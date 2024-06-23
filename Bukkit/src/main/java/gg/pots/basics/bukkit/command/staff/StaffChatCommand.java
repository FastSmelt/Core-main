package gg.pots.basics.bukkit.command.staff;

import gg.pots.basics.bukkit.CoreConstants;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.json.JsonAppender;
import gg.pots.basics.core.profile.ProfileService;
import gg.pots.basics.core.saving.SavingService;
import io.github.nosequel.command.annotation.Command;
import io.github.nosequel.command.bukkit.executor.BukkitCommandExecutor;
import org.apache.commons.lang3.StringUtils;

public class StaffChatCommand {

    private final ProfileService profileModule = ServiceHandler.getInstance().find(ProfileService.class);

    @Command(label = "staffchat", aliases = {"sc"}, permission = "core.staff")
    public void staffChat(BukkitCommandExecutor executor, String message) {
        ServiceHandler.getInstance().find(SavingService.class).getSyncType().publish("message", new JsonAppender()
                .append("permission", "core.staffchat")
                .append("message", CoreConstants.STAFF_CHAT_MESSAGE
                        .replace("%sender%", this.profileModule.find(executor.getPlayer().getName()).getColoredName())
                        .replace("%message%", StringUtils.join(message, " "))
                        .replace("%server%", CoreConstants.SERVER_NAME)
                )
                .get()
        );
    }

    @Command(label = "adminchat", aliases = {"ac"}, permission = "core.admin")
    public void adminChat(BukkitCommandExecutor executor, String message) {
        ServiceHandler.getInstance().find(SavingService.class).getSyncType().publish("message", new JsonAppender()
                .append("permission", "core.adminchat")
                .append("message", CoreConstants.ADMIN_CHAT_MESSAGE
                        .replace("%sender%", this.profileModule.find(executor.getPlayer().getName()).getColoredName())
                        .replace("%message%", StringUtils.join(message, " "))
                        .replace("%server%", CoreConstants.SERVER_NAME)
                )
                .get()
        );
    }

    @Command(label = "devchat", aliases = {"dc"}, permission = "core.developer")
    public void devChat(BukkitCommandExecutor executor, String message) {
        ServiceHandler.getInstance().find(SavingService.class).getSyncType().publish("message", new JsonAppender()
                .append("permission", "core.developerchat")
                .append("message", CoreConstants.DEVELOPER_CHAT_MESSAGE
                        .replace("%sender%", this.profileModule.find(executor.getPlayer().getName()).getColoredName())
                        .replace("%message%", StringUtils.join(message, " "))
                        .replace("%server%", CoreConstants.SERVER_NAME)
                )
                .get()
        );
    }
}