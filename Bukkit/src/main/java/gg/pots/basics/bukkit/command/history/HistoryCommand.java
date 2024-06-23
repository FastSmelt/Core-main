package gg.pots.basics.bukkit.command.history;

import gg.pots.basics.bukkit.command.history.menu.HistoryMenu;
import gg.pots.basics.core.profile.CoreProfile;
import io.github.nosequel.command.annotation.Command;
import io.github.nosequel.command.bukkit.executor.BukkitCommandExecutor;
import org.bukkit.ChatColor;

public class HistoryCommand {

    @Command(label = "history", permission = "core.staff")
    public void execute(BukkitCommandExecutor executor, CoreProfile target) {
        new HistoryMenu(executor.getPlayer(), target).updateMenu();
        executor.sendMessage(ChatColor.YELLOW + "Opening history menu for " + target.getColoredName());
    }
}
