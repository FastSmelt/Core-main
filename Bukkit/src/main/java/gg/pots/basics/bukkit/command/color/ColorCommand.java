package gg.pots.basics.bukkit.command.color;

import gg.pots.basics.bukkit.command.color.menu.ColorMenu;
import io.github.nosequel.command.annotation.Command;
import io.github.nosequel.command.bukkit.executor.BukkitCommandExecutor;

public class ColorCommand {

    @Command(label = "color", userOnly = true)
    public void color(BukkitCommandExecutor executor) {
        new ColorMenu(executor.getPlayer()).updateMenu();
    }
}