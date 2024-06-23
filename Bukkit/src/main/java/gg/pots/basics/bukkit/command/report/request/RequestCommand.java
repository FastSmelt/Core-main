package gg.pots.basics.bukkit.command.report.request;

import gg.pots.basics.bukkit.CoreConstants;

import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.json.JsonAppender;
import gg.pots.basics.core.profile.ProfileService;
import gg.pots.basics.core.saving.SavingService;
import io.github.nosequel.command.annotation.Command;
import io.github.nosequel.command.bukkit.executor.BukkitCommandExecutor;

public class RequestCommand {

    private final ProfileService profileModule = ServiceHandler.getInstance().find(ProfileService.class);

    @Command(label = "request", aliases = {"helpop", "ehelpop"})
    public void execute(BukkitCommandExecutor executor, String message) {
        ServiceHandler.getInstance().find(SavingService.class).getSyncType().publish("requests", new JsonAppender()
                .append("message", String.join(" ", message))
                .append("playerUuid", executor.getPlayer().getUniqueId().toString())
                .append("playerName", this.profileModule.find(executor.getPlayer().getName()).getColoredName())
                .append("server", CoreConstants.SERVER_NAME).get()
        );
    }
}