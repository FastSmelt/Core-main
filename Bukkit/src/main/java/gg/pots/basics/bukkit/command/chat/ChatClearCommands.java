package gg.pots.basics.bukkit.command.chat;

import io.github.nosequel.command.annotation.Command;
import io.github.nosequel.command.bukkit.executor.BukkitCommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import java.util.stream.IntStream;

public class ChatClearCommands {

    @Command(label="clearchat", aliases="cc", permission="core.clearchat")
    public void clearchat(BukkitCommandExecutor executor) {
        Bukkit.getOnlinePlayers().stream()
                .filter(target -> !target.hasPermission("core.staff"))
                .forEach(target -> IntStream.range(0, 500).forEach(i -> target.sendMessage(" ")));

        Bukkit.broadcastMessage(ChatColor.YELLOW + "The global chat has been cleared.");
    }
}
