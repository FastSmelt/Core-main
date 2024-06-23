package gg.pots.basics.bukkit.task;

import gg.pots.basics.bukkit.CoreSpigotPlugin;
import gg.pots.basics.bukkit.util.CC;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.profile.ProfileService;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class BroadcastTask extends BukkitRunnable {

    private final ProfileService profileService = ServiceHandler.getInstance().find(ProfileService.class);

    private final List<String> broadcasts = Arrays.asList(
            CoreSpigotPlugin.instance.broadcastsFile.getStringList("broadcasts"));

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            final CoreProfile coreProfile = this.profileService.find(player.getUniqueId());

            if (coreProfile.isCanViewBroadcasts()) {
                player.sendMessage("");
                player.sendMessage(CC.translate(this.broadcasts.get(ThreadLocalRandom.current().nextInt(
                        this.broadcasts.size()
                ))));
                player.sendMessage("");
            }
        }
    }
}