package gg.pots.basics.core.expirable.grant;

import com.google.gson.JsonObject;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.expirable.Expirable;
import gg.pots.basics.core.json.JsonAppender;
import gg.pots.basics.core.rank.Rank;
import gg.pots.basics.core.rank.RankService;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Grant extends Expirable {

    private final RankService rankService = ServiceHandler.getInstance().find(RankService.class);

    private final UUID uuid;
    private final Rank rank;

    private final String executor;
    private final String reason;

    /**
     * Constructor to create a new {@link Grant}
     *
     * @param uuid the unique identifier.
     * @param rank the rank.
     * @param executor the executor.
     * @param reason the reason.
     * @param expirationDateEpoch the expiration date in epoch.
     */

    public Grant(UUID uuid, Rank rank, String executor, String reason, long expirationDateEpoch) {
        super(System.currentTimeMillis(), expirationDateEpoch == -1L ? -1L : System.currentTimeMillis() + expirationDateEpoch, true);

        this.uuid = uuid == null ? UUID.randomUUID() : uuid;
        this.rank = rank;
        this.executor = executor;
        this.reason = reason;
    }

    public Grant(JsonObject object) {
        super(object);
        this.uuid = UUID.fromString(object.get("uuid").getAsString());
        this.rank = this.rankService.find(UUID.fromString(object.get("rank").getAsString())) == null ? this.rankService.getDefaultRank() : this.rankService.find(UUID.fromString(object.get("rank").getAsString()));
        this.executor = object.get("executor").getAsString();
        this.reason = object.get("reason").getAsString();
    }

    @Override
    public JsonObject toJson() {
        return new JsonAppender(super.toJson())
                .append("uuid", this.uuid.toString())
                .append("rank", this.rank.getUuid().toString())
                .append("executor", this.executor)
                .append("reason", this.reason).get();
    }

    public int getWeight() {
        return this.rank.getWeight();
    }
}
