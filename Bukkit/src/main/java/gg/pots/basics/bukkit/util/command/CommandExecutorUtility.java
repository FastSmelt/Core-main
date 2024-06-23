package gg.pots.basics.bukkit.util.command;

import io.github.nosequel.command.executor.CommandExecutor;

public class CommandExecutorUtility {

    public static void sendMessage(CommandExecutor executor, String[] messages) {
        for (String message : messages) {
            executor.sendMessage(message);
        }
    }
}
