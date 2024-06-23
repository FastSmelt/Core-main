package gg.pots.basics.bukkit.util.logger;

import gg.pots.basics.bukkit.util.CC;
import org.bukkit.Bukkit;

public class LoggerUtility {

    /**
     * Send a message to the console.
     *
     * @param message the message.
     */

    public static void sendMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(CC.translate(message));
    }
}