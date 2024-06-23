package gg.pots.basics.bukkit.chat.impl;

import gg.pots.basics.bukkit.chat.ChatProcedure;
import gg.pots.basics.bukkit.chat.ChatProcedureResult;
import gg.pots.basics.bukkit.chat.ChatProcedureWeight;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class SlowChatChatProcedure implements ChatProcedure {

    private final Map<Player, Long> slowed = new HashMap<>();

    private long slowDuration;

    @Override
    public ChatProcedureResult handle(Player player, String message) {
        if (this.slowed.containsKey(player) && System.currentTimeMillis() - this.slowed.get(player) < 0) {
            player.sendMessage(ChatColor.RED + "The global chat is currently slowed.");
            return new ChatProcedureResult(true, "");
        }

        this.slowed.put(player, System.currentTimeMillis() + this.slowDuration);
        return new ChatProcedureResult(false, "");
    }

    @Override
    public boolean shouldHandle(Player player) {
        return !player.hasPermission("core.staff");
    }

    @Override
    public ChatProcedureWeight getWeight() {
        return ChatProcedureWeight.NORMAL;
    }
}
