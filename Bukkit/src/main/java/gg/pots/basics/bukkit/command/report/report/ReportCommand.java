package gg.pots.basics.bukkit.command.report.report;

import gg.pots.basics.bukkit.CoreConstants;
import gg.pots.basics.bukkit.util.CC;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.json.JsonAppender;
import gg.pots.basics.core.profile.ProfileService;
import gg.pots.basics.core.saving.SavingService;
import io.github.nosequel.command.annotation.Command;
import io.github.nosequel.command.bukkit.executor.BukkitCommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ReportCommand {

    private final ProfileService profileModule = ServiceHandler.getInstance().find(ProfileService.class);

    @Command(label = "report")
    public void execute(BukkitCommandExecutor executor, Player target, String message) {
        if (message.length() <= 1) {
            executor.sendMessage(CC.translate("&cYour report must be at least 2 characters long."));
            return;
        }

        if (target == null) {
            executor.sendMessage(CC.translate("&cThat player is not online."));
            return;
        }

        ServiceHandler.getInstance().find(SavingService.class).getSyncType().publish("reports", new JsonAppender()
                .append("message", message)
                .append("playerUuid", executor.getPlayer().getUniqueId().toString())
                .append("playerName", this.profileModule.find(executor.getPlayer().getName()).getColoredName())
                .append("targetName", this.profileModule.find(target.getName()).getColoredName())
                .append("server", CoreConstants.SERVER_NAME).get()
        );
    }
}