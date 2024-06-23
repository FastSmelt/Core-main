package gg.pots.basics.bukkit.chat.impl.grant;

import gg.pots.basics.bukkit.chat.ChatProcedure;
import gg.pots.basics.bukkit.chat.ChatProcedureResult;
import gg.pots.basics.bukkit.chat.ChatProcedureWeight;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.expirable.grant.Grant;
import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.profile.ProfileService;
import gg.pots.basics.core.rank.Rank;
import gg.pots.basics.core.rank.RankService;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class GrantSetReasonProcedure implements ChatProcedure {

    private final Map<Player, GrantProcedureData> dataMap = new HashMap<>();

    private final RankService rankModule = ServiceHandler.getInstance().find(RankService.class);
    private final ProfileService profileModule = ServiceHandler.getInstance().find(ProfileService.class);

    @Override
    public ChatProcedureResult handle(Player player, String message) {
        final GrantProcedureData data = this.dataMap.get(player);

        if (data != null && !message.equalsIgnoreCase("cancel")) {
            final CoreProfile executor = this.profileModule.find(player.getUniqueId());
            final CoreProfile target = data.getTarget();
            final Rank rank = data.getRank();
            final long duration = data.getDuration() == -1L ? -1L : System.currentTimeMillis() + data.getDuration();

            data.setReason(message);
            target.getGrants().add(new Grant(UUID.randomUUID(), rank, executor.getName(), data.getReason(), duration));

            this.profileModule.syncProfile(target);
            this.profileModule.find(target.getUuid()).getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou have been &agranted &ethe rank " + rank.getDisplayName() + "&f."));
        }

        this.dataMap.remove(player);
        return new ChatProcedureResult(true, "");
    }

    @Override
    public boolean shouldHandle(Player player) {
        return dataMap.containsKey(player);
    }

    @Override
    public ChatProcedureWeight getWeight() {
        return ChatProcedureWeight.HIGH;
    }
}
