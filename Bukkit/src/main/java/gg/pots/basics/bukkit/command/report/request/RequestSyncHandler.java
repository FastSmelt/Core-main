package gg.pots.basics.bukkit.command.report.request;

import com.google.gson.JsonObject;
import gg.pots.basics.core.saving.sync.SyncHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RequestSyncHandler implements SyncHandler {

    private final Map<UUID, Long> cooldown = new HashMap<>();

    @Override
    public void incoming(String channel, JsonObject object) {
        final UUID playerUuid = UUID.fromString(object.get("playerUuid").getAsString());

        if (this.cooldown.containsKey(playerUuid) && System.currentTimeMillis() - this.cooldown.get(playerUuid) < 0) {
            this.cooldown.remove(playerUuid);
        }

        if (!this.cooldown.containsKey(playerUuid) || System.currentTimeMillis() - this.cooldown.get(playerUuid) < 0) {
            final String playerName = object.get("playerName").getAsString();
            final String message = object.get("message").getAsString();
            final String server = object.get("server").getAsString();

            Bukkit.getOnlinePlayers().stream()
                    .filter(player -> player.hasPermission("core.staff"))
                    .forEach(player -> player.sendMessage(new String[]{
                            ChatColor.BLUE + "[Request] " + ChatColor.GRAY + "(" + server + ") " + playerName + ChatColor.AQUA + " has requested assistance",
                            ChatColor.BLUE + "  Reason: " + ChatColor.AQUA + message
                    }));

            this.cooldown.put(playerUuid, System.currentTimeMillis());
        } else {
            if (Bukkit.getPlayer(playerUuid) != null) {
                Bukkit.getPlayer(playerUuid).sendMessage(ChatColor.RED + "You are still on a cooldown for that action.");
            }
        }
    }

    @Override
    public String getChannel() {
        return "requests";
    }
}