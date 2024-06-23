package gg.pots.basics.bukkit;

import gg.pots.basics.bukkit.util.CC;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

@UtilityClass
public class CoreConstants {

    private final FileConfiguration config = CoreSpigotPlugin.getPlugin(CoreSpigotPlugin.class).getConfig();

    public String SERVER_NAME = config.getString("server.id");

    public String MONGO_HOSTNAME = config.getString("database.mongodb.hostname");
    public Integer MONGO_PORT = config.getInt("database.mongodb.port");
    public String MONGO_DATABASE = config.getString("database.mongodb.database");

    public String REDIS_HOSTNAME = config.getString("database.redis.hostname");
    public Integer REDIS_PORT = config.getInt("database.redis.port");

    public String KICKED_INCOMPLETE_PROFILE = String.join("\n", ChatColor.RED + "Your profile has not loaded properly", ChatColor.RED + "If this problem persists, contact a staff member.");

    public String KICKED_BLACKLIST = String.join("\n",
            ChatColor.RED + "You have been blacklisted from the network and are unable to join",
            "",
            ChatColor.RED + "This punishment is permanent and cannot be appealed."
    );

    public String KICKED_BAN = String.join("\n",
            ChatColor.RED + "You have been banned from this network and are unable to join",
            "",
            ChatColor.RED + "If you think this punishment is unjustified,",
            ChatColor.RED + "you may appeal at pots.gg/discord"
    );

    public String STAFF_CHAT_MESSAGE = ChatColor.BLUE + "[Staff]" + ChatColor.GRAY + " (%server%) " + ChatColor.DARK_PURPLE + "%sender%" + ChatColor.GRAY + ": " + ChatColor.WHITE + "%message%";
    public String ADMIN_CHAT_MESSAGE = ChatColor.RED + "[Admin]" + ChatColor.GRAY + " (%server%) " + ChatColor.DARK_RED + "%sender%" + ChatColor.GRAY + ": " + ChatColor.WHITE + "%message%";
    public String DEVELOPER_CHAT_MESSAGE = ChatColor.AQUA + "[Dev]" + ChatColor.GRAY + " (%server%) " + ChatColor.DARK_AQUA + "%sender%" + ChatColor.GRAY + ": " + ChatColor.WHITE + "%message%";

    public String CHAT_CANCELLED_MUTED = ChatColor.RED + "You cannot chat while you are muted.";

    public static String DISCORD_MESSAGE = CC.translate(CoreSpigotPlugin.instance.messagesFile.getString("messages.discord"));
    public static String STORE_MESSAGE = CC.translate(CoreSpigotPlugin.instance.messagesFile.getString("messages.store"));
    public static String WEBSITE_MESSAGE = CC.translate(CoreSpigotPlugin.instance.messagesFile.getString("messages.website"));
}
