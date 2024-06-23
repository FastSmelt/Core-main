package gg.pots.basics.bukkit.command.toggle;

import gg.pots.basics.bukkit.util.CC;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.profile.ProfileService;
import io.github.nosequel.command.annotation.Command;
import io.github.nosequel.command.bukkit.executor.BukkitCommandExecutor;

public class TogglePMCommand {

    private final ProfileService profileService = ServiceHandler.getInstance().find(ProfileService.class);

    @Command(label = "toggleprivatemessage", aliases = {"togglepms", "tpms", "tpm"}, userOnly = true)
    public void toggleMessages(BukkitCommandExecutor executor) {
        final CoreProfile coreProfile = this.profileService.find(executor.getPlayer().getUniqueId());

        coreProfile.setTogglePrivateMessages(!coreProfile.isTogglePrivateMessages());
        executor.sendMessage(CC.translate("&eYou have " + (coreProfile.isTogglePrivateMessages() ? "&aEnabled" : "&cDisabled") + " &eyour private messages."));
    }
}
