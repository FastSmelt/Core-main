package gg.pots.basics.bukkit.command.chat;

import gg.pots.basics.bukkit.chat.ChatProcedureModule;
import gg.pots.basics.bukkit.chat.impl.SlowChatChatProcedure;
import gg.pots.basics.core.ServiceHandler;
import io.github.nosequel.command.annotation.Command;
import io.github.nosequel.command.bukkit.executor.BukkitCommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ChatSlowCommand {

    private final ChatProcedureModule procedureModule = ServiceHandler.getInstance().find(ChatProcedureModule.class);
    private final SlowChatChatProcedure slowProcedure = procedureModule.find(SlowChatChatProcedure.class);

    @Command(label = "slowchat", aliases = {"slow", "chatslow"}, permission = "core.slowchat")
    public void slowchat(BukkitCommandExecutor executor, Integer seconds) {
        this.slowProcedure.setSlowDuration(seconds * 1000L);

        if (seconds == 0) {
            Bukkit.broadcastMessage(ChatColor.YELLOW + "The global chat has been un-slowed.");
        } else {
            Bukkit.broadcastMessage(ChatColor.YELLOW + "The chat has been slowed, chat slow duration is " + ChatColor.RED + seconds + ChatColor.YELLOW + " seconds.");
        }
    }
}