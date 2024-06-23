package gg.pots.basics.bukkit.command.punishment;

import gg.pots.basics.bukkit.util.CC;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.expirable.punishment.Punishment;
import gg.pots.basics.core.expirable.punishment.PunishmentType;
import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.profile.ProfileService;
import io.github.nosequel.command.annotation.Command;
import io.github.nosequel.command.annotation.Param;
import io.github.nosequel.command.bukkit.executor.BukkitCommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BlacklistCommand {

    private final ProfileService profileService = ServiceHandler.getInstance().find(ProfileService.class);

    @Command(label = "blacklist", permission = "core.blacklist")
    public void blacklist(BukkitCommandExecutor executor, CoreProfile target, @Param(name = "reason", value = "No reason specified.") String reason) {
        final CoreProfile executorProfile = this.profileService.find(executor.getPlayer().getUniqueId());

        if (target.getRank().getWeight() >= executorProfile.getRank().getWeight()) {
            executor.sendMessage("&cYou cannot blacklist this player.");
            return;
        }

        if (target.getActivePunishments(PunishmentType.BLACKLIST).stream().anyMatch(Punishment::isActive)) {
            executor.sendMessage("&cThis player is already blacklisted.");
            return;
        }

        Player player = Bukkit.getPlayer(target.getPlayer().getUniqueId());
        if (player != null) {
            player.kickPlayer(CC.translate("&cYou have been blacklisted for &e" + reason + "\n &cThis punishment is permanent and cannot be appealed."));
        }

        target.getPunishments().add(new Punishment(UUID.randomUUID(), PunishmentType.BLACKLIST, reason, executor.getPlayer().getName(), -1L));
        this.profileService.syncProfile(target);

        Bukkit.broadcastMessage(CC.translate(target.getColoredName() + " &ehas been &4blacklisted &eby " + executorProfile.getColoredName()));
        executor.sendMessage(CC.translate("&eYou have &4blacklisted " + target.getColoredName()));
    }

    @Command(label = "unblacklist", permission = "core.unblacklist")
    public void unblacklist(BukkitCommandExecutor executor, CoreProfile target) {
        final CoreProfile executorProfile = this.profileService.find(executor.getPlayer().getUniqueId());

        if (target.getRank().getWeight() >= executorProfile.getRank().getWeight()) {
            executor.sendMessage("&cYou cannot unblacklist this player.");
            return;
        }

        if (target.getActivePunishments(PunishmentType.BLACKLIST).stream().noneMatch(Punishment::isActive)) {
            executor.sendMessage("&cThis player is not blacklisted.");
            return;
        }

        target.getActivePunishments(PunishmentType.BLACKLIST).stream()
                .filter(Punishment::isActive)
                .forEach(punishment -> punishment.setActive(false));

        this.profileService.syncProfile(target);

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("core.staff")) {
                player.sendMessage(CC.translate(target.getColoredName() + " &ehas been &cunblacklisted &eby " + executorProfile.getColoredName()));
            }
        }

        executor.sendMessage(CC.translate("&eYou have &cunblacklisted " + target.getColoredName()));
    }
}
