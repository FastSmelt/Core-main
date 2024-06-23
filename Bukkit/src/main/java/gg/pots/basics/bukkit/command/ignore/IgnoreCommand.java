package gg.pots.basics.bukkit.command.ignore;

import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.profile.ProfileService;
import io.github.nosequel.command.annotation.Command;
import io.github.nosequel.command.bukkit.executor.BukkitCommandExecutor;

public class IgnoreCommand {

    private final ProfileService profileService = ServiceHandler.getInstance().find(ProfileService.class);

    @Command(label = "ignore", userOnly = true)
    public void ignore(BukkitCommandExecutor executor, CoreProfile target) {
        final CoreProfile coreProfile = this.profileService.find(executor.getPlayer().getUniqueId());

        if (coreProfile.getIgnoredPlayers().contains(target.getName())) {
            coreProfile.getIgnoredPlayers().remove(target.getName());
            executor.sendMessage("&cYou are no longer ignoring " + target.getColoredName() + ".");
        } else {
            coreProfile.getIgnoredPlayers().add(target.getName());
            executor.sendMessage("&aYou are now ignoring " + target.getColoredName() + ".");
        }
    }
}
