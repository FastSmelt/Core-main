package gg.pots.basics.bukkit.command.punishment;

import gg.pots.basics.bukkit.util.CC;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.profile.ProfileService;
import io.github.nosequel.command.annotation.Command;
import io.github.nosequel.command.annotation.Param;
import io.github.nosequel.command.bukkit.executor.BukkitCommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class KickCommand {

    private final ProfileService profileService = ServiceHandler.getInstance().find(ProfileService.class);

    @Command(label = "kick", permission = "core.kick")
    public void kick(BukkitCommandExecutor executor, CoreProfile target, @Param(name = "reason", value = "No reason specified.") String reason) {
        final CoreProfile executorProfile = this.profileService.find(executor.getPlayer().getUniqueId());

        if (target.getRank().getWeight() >= executorProfile.getRank().getWeight()) {
            executor.sendMessage("&cYou cannot kick this player.");
            return;
        }

        Player player = Bukkit.getPlayer(target.getPlayer().getUniqueId());
        if (player != null) {
            player.kickPlayer(CC.translate("&cYou have been kicked for &e" + reason));
        }

        Bukkit.broadcastMessage(CC.translate("&e" + target.getPlayer().getName() + " &ahas been kicked for &e" + reason));
        executor.sendMessage(CC.translate("&e" + target.getPlayer().getName() + " &ahas been kicked for &e" + reason));
    }
}
