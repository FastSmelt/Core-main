package gg.pots.basics.bukkit.command.alts;

import gg.pots.basics.bukkit.util.CC;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.profile.ProfileService;
import io.github.nosequel.command.annotation.Command;
import io.github.nosequel.command.bukkit.executor.BukkitCommandExecutor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class AltsCommand {

    private final ProfileService profileService = ServiceHandler.getInstance().find(ProfileService.class);

    @Command(label = "alts", permission = "core.alts", userOnly = true)
    public void checkAlts(BukkitCommandExecutor executor, CoreProfile target) {
        this.findAlts(executor.getPlayer(), target);
    }

    private void findAlts(Player player, CoreProfile target) {
        final List<CoreProfile> alts = this.profileService.stream()
                .filter(profile -> profile.getIpAddress().equals(target.getIpAddress()))
                .collect(Collectors.toList());

        if (!alts.isEmpty()) {
            StringBuilder altList = new StringBuilder();

            if (alts.size() == 1) {
                player.sendMessage(CC.translate(target.getColoredName() + " &ehas no alts."));
                return;
            }

            for (CoreProfile alt : alts) {
                altList.append(alt.getName()).append("\n");
            }

            player.sendMessage(CC.translate(target.getColoredName() + "'s &ealts: &c\n" + altList));
        }
    }
}
