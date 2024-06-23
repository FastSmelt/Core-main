package gg.pots.basics.core.rank.sync;

import com.google.gson.JsonObject;
import gg.pots.basics.core.rank.Rank;
import gg.pots.basics.core.rank.RankService;
import gg.pots.basics.core.saving.sync.SyncHandler;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class RankDeleteHandler implements SyncHandler {

    private final RankService rankService;

    @Override
    public void incoming(String channel, JsonObject object) {
        if (channel.equals("rank-delete")) {
            final UUID rankUuid = UUID.fromString(object.get("uuid").getAsString());
            final Rank rank = this.rankService.find(rankUuid);

            if (rank != null) {
                this.rankService.deleteRank(rank);
            }
        }
    }

    @Override
    public String getChannel() {
        return "rank-delete";
    }
}