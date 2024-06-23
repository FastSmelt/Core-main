package gg.pots.basics.bukkit.command.gem;

import gg.pots.basics.bukkit.util.CC;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.profile.ProfileService;
import io.github.nosequel.command.annotation.Command;
import io.github.nosequel.command.bukkit.executor.BukkitCommandExecutor;
import io.github.nosequel.command.exception.ConditionFailedException;

public class GemCommands {

    private final ProfileService profileService = ServiceHandler.getInstance().find(ProfileService.class);

    @Command(label = "gems", userOnly = true)
    public void viewBalance(BukkitCommandExecutor executor) {
        final int gems = this.profileService.find(executor.getPlayer().getUniqueId()).getGems();
        executor.sendMessage(CC.translate("&eYou currently have &a" + gems + " &egems."));
    }

    @Command(label = "setgems", permission = "core.admin", userOnly = true)
    public void setBalance(BukkitCommandExecutor executor, CoreProfile target, Integer amount) throws ConditionFailedException {
        final CoreProfile profile = this.profileService.find(target.getUuid());

        if (amount < 1) {
            throw new ConditionFailedException(CC.translate("&cYou cannot set a player's gems to less than 1."));
        }

        profile.setGems(amount);
        this.profileService.syncProfile(profile);
        executor.sendMessage(CC.translate("&eYou have set &a" + target.getName() + "'s &egems to &a" + amount + "&e."));
    }
}
