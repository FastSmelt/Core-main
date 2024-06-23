package gg.pots.basics.bukkit.listener;

import gg.pots.basics.bukkit.CoreConstants;
import gg.pots.basics.bukkit.CoreSpigotPlugin;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.expirable.grant.Grant;
import gg.pots.basics.core.expirable.punishment.PunishmentType;
import gg.pots.basics.core.json.JsonAppender;
import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.profile.ProfileService;
import gg.pots.basics.core.saving.SavingService;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;

public class PlayerListener implements Listener {

    private final ProfileService profileModule = ServiceHandler.getInstance().find(ProfileService.class);

    @EventHandler
    public void onPreJoin(AsyncPlayerPreLoginEvent event) {
        final CoreProfile profile = this.profileModule.findOrElseMake(event.getUniqueId(), event.getName());

        profile.setName(event.getName());
        profile.setIpAddress(event.getAddress().getHostAddress());

        if (profile.getGrants().isEmpty()) {
            profile.addDefaultRank();
        }

        if (!profile.isComplete()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, CoreConstants.KICKED_INCOMPLETE_PROFILE);
        }

        if (profile.getActivePunishment(PunishmentType.BLACKLIST).isPresent()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, CoreConstants.KICKED_BLACKLIST);
        }

        if (profile.getActivePunishment(PunishmentType.BAN).isPresent()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, CoreConstants.KICKED_BAN);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        final Player player = event.getPlayer();
        final CoreProfile profile = this.profileModule.findOrElseMake(player.getUniqueId(), null);

        this.profileModule.syncProfile(profile);
        this.profileModule.saveProfileAsync(profile);

        profile.setLastJoin(System.currentTimeMillis());

        {
            final PermissionAttachment attachment = player.addAttachment(CoreSpigotPlugin.getPlugin(CoreSpigotPlugin.class));

            for (Grant grant : profile.getGrants()) {
                if (grant.isActive()) {
                    for (String permission : grant.getRank().getAllPermissions()) {
                        attachment.setPermission(permission, true);
                    }
                }
            }

            for (String permission : profile.getAllPermissions()) {
                attachment.setPermission(permission, true);
            }

            player.recalculatePermissions();
        }

        player.setDisplayName(profile.getColoredName());

        if (player.hasPermission("core.staff")) {
            ServiceHandler.getInstance().find(SavingService.class).getSyncType().publish("staff-join", new JsonAppender()
                    .append("server", CoreConstants.SERVER_NAME)
                    .append("name", player.getName()).get());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        final Player player = event.getPlayer();
        final CoreProfile profile = this.profileModule.findOrElseMake(player.getUniqueId(), null);

        profile.setLastJoin(System.currentTimeMillis());

        this.profileModule.syncProfile(profile);
        this.profileModule.saveProfileAsync(profile);

        if (player.hasPermission("core.staff")) {
            ServiceHandler.getInstance().find(SavingService.class).getSyncType().publish("staff-quit", new JsonAppender()
                    .append("server", CoreConstants.SERVER_NAME)
                    .append("name", player.getName()).get());
        }
    }

    private final String[] blockedCommands = new String[] {
            "/me",
            "/bukkit:me",
            "/minecraft:me",
            "//calc",
            "//eval",
            "/tellraw"
    };

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        for (String blockedCommand : this.blockedCommands) {
            if (event.getMessage().startsWith(blockedCommand)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "You can't execute this command.");
                break;
            }
        }
    }
}

