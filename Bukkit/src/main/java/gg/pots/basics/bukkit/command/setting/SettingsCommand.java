package gg.pots.basics.bukkit.command.setting;

import gg.pots.basics.bukkit.command.setting.menu.SettingsMenu;
import gg.pots.basics.bukkit.util.CC;
import io.github.nosequel.command.annotation.Command;
import io.github.nosequel.command.bukkit.executor.BukkitCommandExecutor;

public class SettingsCommand {

    @Command(label = "settings")
    public void settings(BukkitCommandExecutor executor) {
        new SettingsMenu(executor.getPlayer()).updateMenu();

        executor.sendMessage(CC.translate("&eOpening settings menu..."));
    }
}