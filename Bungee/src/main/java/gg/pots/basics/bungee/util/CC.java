package gg.pots.basics.bungee.util;

import net.md_5.bungee.api.ChatColor;

public class CC {

    /**
     * Translate a {@link String} with color codes.
     *
     * @param message the message.
     * @return the translated message.
     */

    public static String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
