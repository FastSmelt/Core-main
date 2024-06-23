package gg.pots.basics.bukkit.command.grant;

import gg.pots.basics.bukkit.command.grant.menu.grants.GrantsMenu;
import gg.pots.basics.bukkit.util.CC;
import gg.pots.basics.core.profile.CoreProfile;
import io.github.nosequel.command.annotation.Command;
import io.github.nosequel.command.bukkit.executor.BukkitCommandExecutor;
import org.bukkit.entity.Player;

public class GrantsCommand {

    @Command(label = "grants", permission = "core.grants", userOnly = true)
    public void grants(BukkitCommandExecutor executor, CoreProfile target) {
        if (!(executor.getSender() instanceof Player)) {
            executor.sendMessage(CC.translate("&cYou must be a player to execute this command."));
        }

        new GrantsMenu(executor.getPlayer(), target).updateMenu();
        executor.sendMessage(CC.translate("&eViewing " + target.getColoredName() + "'s &egrants..."));
    }
}