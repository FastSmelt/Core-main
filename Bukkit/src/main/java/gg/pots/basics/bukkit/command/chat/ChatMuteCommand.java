package gg.pots.basics.bukkit.command.chat;

import gg.pots.basics.bukkit.chat.ChatProcedureModule;
import gg.pots.basics.bukkit.chat.impl.MuteChatChatProcedure;
import gg.pots.basics.core.ServiceHandler;
import io.github.nosequel.command.annotation.Command;
import io.github.nosequel.command.bukkit.executor.BukkitCommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ChatMuteCommand {

    private final ChatProcedureModule procedureModule = ServiceHandler.getInstance().find(ChatProcedureModule.class);
    private final MuteChatChatProcedure muteProcedure = procedureModule.find(MuteChatChatProcedure.class);

    @Command(label="mutechat", aliases = {"mc", "chatmute"}, permission = "core.mutechat")
    public void execute(BukkitCommandExecutor executor) {
        this.muteProcedure.setMuted(!this.muteProcedure.isMuted());
        Bukkit.broadcastMessage(ChatColor.YELLOW + "The global chat has been " + (this.muteProcedure.isMuted() ? "muted." : "unmuted"));
    }
}
