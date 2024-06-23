package gg.pots.basics.bukkit.message;

import com.google.gson.JsonObject;
import gg.pots.basics.core.saving.sync.SyncHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class StringSyncHandler implements SyncHandler {

    @Override
    public void incoming(String channel, JsonObject object) {
        if (object.has("permission")) {
            Bukkit.getOnlinePlayers().stream()
                    .filter(player -> player.hasPermission(object.get("permission").getAsString()))
                    .forEach(player -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', object.get("message").getAsString())));
        } else {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', object.get("message").getAsString()));
        }
    }

    @Override
    public String getChannel() {
        return "message";
    }
}