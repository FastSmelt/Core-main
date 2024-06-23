package gg.pots.basics.bukkit.util.color;

import gg.pots.basics.core.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

import java.util.HashMap;
import java.util.Map;

public class ColorUtility {

    private static final Map<ChatColor, DyeColor> colorMap = new HashMap<>();

    static {
        colorMap.put(ChatColor.BLUE, DyeColor.BLUE);
        colorMap.put(ChatColor.AQUA, DyeColor.CYAN);
        colorMap.put(ChatColor.BLACK, DyeColor.BLACK);
        colorMap.put(ChatColor.DARK_AQUA, DyeColor.CYAN);
        colorMap.put(ChatColor.DARK_BLUE, DyeColor.BLUE);
        colorMap.put(ChatColor.DARK_GREEN, DyeColor.GREEN);
        colorMap.put(ChatColor.GREEN, DyeColor.LIME);
        colorMap.put(ChatColor.DARK_PURPLE, DyeColor.PURPLE);
        colorMap.put(ChatColor.LIGHT_PURPLE, DyeColor.PINK);
        colorMap.put(ChatColor.RED, DyeColor.RED);
        colorMap.put(ChatColor.DARK_RED, DyeColor.RED);
        colorMap.put(ChatColor.YELLOW, DyeColor.YELLOW);
        colorMap.put(ChatColor.WHITE, DyeColor.WHITE);
        colorMap.put(ChatColor.GOLD, DyeColor.ORANGE);
        colorMap.put(ChatColor.GRAY, DyeColor.GRAY);
        colorMap.put(ChatColor.DARK_GRAY, DyeColor.GRAY);
    }

    private ColorUtility() {
        throw new AssertionError("ColorUtil cannot be instantiated.");
    }

    /**
     * Find a dye color in the color map.
     *
     * @param color the color to get the dye color
     * @return the dye color
     */
    public static DyeColor findDyeColor(ChatColor color) {
        return colorMap.getOrDefault(color, DyeColor.WHITE);
    }

    /**
     * Get a {@link ChatColor} object.
     *
     * @param rank the rank to get the color by
     * @return the color
     */
    public static ChatColor getColorByRank(Rank rank) {
        return ChatColor.getByChar(rank.getColor().getChar());
    }
}