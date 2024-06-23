package gg.pots.basics.bukkit.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CC {

    /**
     * Translates a string using an alternate color code character into a string that uses the internal.
     *
     * @param string The string to translate.
     * @return The translated string.
     */

    public static String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Translates a string using an alternate color code character into a string that uses the internal.
     *
     * @param strings The strings to translate.
     * @return The translated strings.
     */

    public static String[] translate(String... strings) {
        return translate(Arrays.asList(strings))
                .toArray(new String[
                        strings.length
                        ]
                );
    }

    /**
     * Translates a string using an alternate color code character into a string that uses the internal.
     *
     * @param strings The strings to translate.
     * @return The translated strings.
     */

    public static List<String> translate(List<String> strings) {
        return strings.stream()
                .map(CC::translate)
                .collect(Collectors.toList());
    }
}