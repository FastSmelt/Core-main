package gg.pots.basics.bukkit.chat.impl;

import gg.pots.basics.bukkit.chat.ChatProcedure;
import gg.pots.basics.bukkit.chat.ChatProcedureResult;
import gg.pots.basics.bukkit.chat.ChatProcedureWeight;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
public class MuteChatChatProcedure implements ChatProcedure {

    private boolean muted;

    @Override
    public ChatProcedureResult handle(Player player, String message) {
        return new ChatProcedureResult(this.muted, "");
    }

    @Override
    public boolean shouldHandle(Player player) {
        return !player.hasPermission("core.staff");
    }

    @Override
    public ChatProcedureWeight getWeight() {
        return ChatProcedureWeight.HIGH;
    }
}
