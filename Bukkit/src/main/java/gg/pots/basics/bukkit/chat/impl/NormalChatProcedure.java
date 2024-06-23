package gg.pots.basics.bukkit.chat.impl;

import gg.pots.basics.bukkit.chat.ChatProcedure;
import gg.pots.basics.bukkit.chat.ChatProcedureResult;
import gg.pots.basics.bukkit.chat.ChatProcedureWeight;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.profile.ProfileService;
import gg.pots.basics.core.rank.Rank;
import gg.pots.basics.core.tag.Tag;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class NormalChatProcedure implements ChatProcedure {

    private final ProfileService profileModule = ServiceHandler.getInstance().find(ProfileService.class);

    @Override
    public ChatProcedureResult handle(Player player, String message) {
        final CoreProfile profile = this.profileModule.find(player.getUniqueId());
        final Rank rank = profile.getPrimaryGrant().getRank();
        final Tag tag = profile.getActiveTag();
        final List<MetadataValue> metadata = player.getMetadata("color");

        if (!metadata.isEmpty()) {
            final ChatColor color = ChatColor.valueOf(metadata.get(0).asString());

            return new ChatProcedureResult(
                    false,
                    ChatColor.translateAlternateColorCodes('&', rank.getPrefix() + player.getName())
                            + ChatColor.GRAY + ChatColor.RESET + ": " + (tag != null ? tag.getPrefix() + " " : "") + color + message.replace("%", "%%")
            );
        }

        return new ChatProcedureResult(
                false,
                ChatColor.translateAlternateColorCodes('&', rank.getPrefix() + player.getName())
                        + ChatColor.GRAY + ": " + ChatColor.RESET + (tag != null ? tag.getPrefix() + " ": "") + profile.getCustomColor() + message.replace("%", "%%")
        );
    }

    @Override
    public boolean shouldHandle(Player player) {
        return this.profileModule.find(player.getUniqueId()) != null;
    }

    @Override
    public ChatProcedureWeight getWeight() {
        return ChatProcedureWeight.CHAT;
    }
}