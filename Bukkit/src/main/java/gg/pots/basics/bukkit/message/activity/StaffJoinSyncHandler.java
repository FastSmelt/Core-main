package gg.pots.basics.bukkit.message.activity;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.profile.ProfileService;
import gg.pots.basics.core.saving.sync.SyncHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

@RequiredArgsConstructor
public class StaffJoinSyncHandler implements SyncHandler {

    private final ProfileService profileModule = ServiceHandler.getInstance().find(ProfileService.class);

    @Override
    public void incoming(String channel, JsonObject object) {
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.hasPermission("core.staff"))
                .forEach(player -> player.sendMessage(ChatColor.BLUE + "[Staff] "
                        + this.profileModule.find(object.get("name").getAsString()).getColoredName()
                        + ChatColor.AQUA + " has joined "
                        + ChatColor.GRAY + "(" + object.get("server").getAsString() + ") "));
    }

    @Override
    public String getChannel() {
        return "staff-join";
    }
}