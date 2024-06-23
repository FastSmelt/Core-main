package gg.pots.basics.bukkit.chat.impl;

import gg.pots.basics.bukkit.CoreConstants;
import gg.pots.basics.bukkit.chat.ChatProcedure;
import gg.pots.basics.bukkit.chat.ChatProcedureResult;
import gg.pots.basics.bukkit.chat.ChatProcedureWeight;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.expirable.punishment.PunishmentType;
import gg.pots.basics.core.profile.ProfileService;
import org.bukkit.entity.Player;

public class MutePunishmentChatProcedure implements ChatProcedure {

    private final ProfileService profileModule = ServiceHandler.getInstance().find(ProfileService.class);

    @Override
    public ChatProcedureResult handle(Player player, String message) {
        player.sendMessage(CoreConstants.CHAT_CANCELLED_MUTED);
        return new ChatProcedureResult(true, "");
    }

    @Override
    public boolean shouldHandle(Player player) {
        return !profileModule.find(player.getUniqueId()).getActivePunishments(PunishmentType.MUTE).isEmpty();
    }

    @Override
    public ChatProcedureWeight getWeight() {
        return ChatProcedureWeight.SEVERE;
    }
}
