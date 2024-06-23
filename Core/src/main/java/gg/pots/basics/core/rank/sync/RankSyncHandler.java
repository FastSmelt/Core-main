package gg.pots.basics.core.rank.sync;

import com.google.gson.JsonObject;
import gg.pots.basics.core.rank.Rank;
import gg.pots.basics.core.rank.RankService;
import gg.pots.basics.core.saving.sync.SyncHandler;
import gg.pots.basics.core.json.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;

import java.util.UUID;

@RequiredArgsConstructor
public class RankSyncHandler implements SyncHandler {

    private final RankService rankService;

    @Override
    public void incoming(String channel, JsonObject object) {
        if (channel.equals(this.getChannel())) {
            final UUID rankUuid = UUID.fromString(object.get("uuid").getAsString());
            final String rankName = object.get("name").getAsString();
            final Rank rank = rankService.findOrMake(rankUuid, rankName);

            rank.setName(object.get("name").getAsString());
            rank.setPrefix(object.get("prefix").getAsString());
            rank.setColor(ChatColor.valueOf(object.get("color").getAsString()));
            rank.setDefaultRank(object.get("defaultRank").getAsBoolean());
            rank.setDisplayName(object.get("displayName").getAsString());
            rank.setWeight(object.get("weight").getAsInt());
            rank.setHidden(object.get("hidden").getAsBoolean());

            JsonUtils.getParser().parse(object.get("permissions").getAsString())
                    .getAsJsonArray().forEach(element -> rank.getPermissions().add(element.getAsString()));

            JsonUtils.getParser().parse(object.get("inherits").getAsString())
                    .getAsJsonArray().forEach(element -> rank.getInherits().add(UUID.fromString(element.getAsString())));
        }
    }

    @Override
    public String getChannel() {
        return "ranks";
    }
}