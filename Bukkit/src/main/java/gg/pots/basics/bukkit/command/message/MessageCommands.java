package gg.pots.basics.bukkit.command.message;

import gg.pots.basics.bukkit.util.CC;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.profile.ProfileService;
import io.github.nosequel.command.annotation.Command;
import io.github.nosequel.command.bukkit.executor.BukkitCommandExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MessageCommands {

    private final ProfileService profileService = ServiceHandler.getInstance().find(ProfileService.class);
    private final Map<UUID, UUID> history = new HashMap<>();

    @Command(label = "message", aliases = "msg", userOnly = true)
    public void message(BukkitCommandExecutor executor, CoreProfile target, String message) {
        final CoreProfile recipient = this.profileService.find(executor.getPlayer().getName());

        if (!target.isTogglePrivateMessages()) {
            executor.sendMessage("&cThat player has private messages toggled off.");
            return;
        }

        if (recipient.getIgnoredPlayers().equals(target.getPlayer().getUniqueId())) {
            executor.sendMessage("&cYou have ignored that player.");
            return;
        }

        executor.sendMessage(CC.translate("&e(To " + target.getColoredName() + "&e) " + message));
        target.getPlayer().sendMessage(CC.translate("&e(From " + recipient.getColoredName() + "&e) " + message));

        this.history.put(executor.getPlayer().getUniqueId(), target.getPlayer().getUniqueId());
        this.history.put(target.getPlayer().getUniqueId(), executor.getPlayer().getUniqueId());
    }

    @Command(label = "reply", aliases = "r", userOnly = true)
    public void reply(BukkitCommandExecutor executor, String message) {
        final CoreProfile recipient = this.profileService.find(executor.getPlayer().getName());

        if (!this.history.containsKey(executor.getPlayer().getUniqueId())) {
            executor.sendMessage("&cYou have no one to reply to.");
            return;
        }

        if (!recipient.isTogglePrivateMessages()) {
            executor.sendMessage("&cThat player has private messages toggled off.");
            return;
        }

        final CoreProfile target = this.profileService.find(this.history.get(executor.getPlayer().getUniqueId()));

        executor.sendMessage(CC.translate("&e(To " + target.getColoredName() + "&e) " + message));
        target.getPlayer().sendMessage(CC.translate("&e(From " + recipient.getColoredName() + "&e) " + message));

        this.history.put(executor.getPlayer().getUniqueId(), target.getPlayer().getUniqueId());
        this.history.put(target.getPlayer().getUniqueId(), executor.getPlayer().getUniqueId());
    }
}
