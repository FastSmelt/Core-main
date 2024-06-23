package gg.pots.basics.core.expirable.punishment;

import com.google.gson.JsonObject;
import gg.pots.basics.core.expirable.Expirable;
import gg.pots.basics.core.json.JsonAppender;
import lombok.Getter;
import org.bukkit.command.CommandSender;

import java.util.UUID;

@Getter
public class Punishment extends Expirable {

    private final UUID uuid;
    private final PunishmentType type;

    private final String reason;
    private final String executor;

    public Punishment(UUID uuid, PunishmentType type, String reason, String executor, long expirationDateEpoch) {
        super(System.currentTimeMillis(), expirationDateEpoch == -1L ? -1L : System.currentTimeMillis() + expirationDateEpoch, true);

        this.uuid = uuid == null ? UUID.randomUUID() : uuid;
        this.type = type;
        this.reason = reason;
        this.executor = executor;
    }

    /**
     * Constructor for loading a {@link Punishment} object from a {@link JsonObject}
     *
     * @param object the json object to load the Punishment from.
     */

    public Punishment(JsonObject object) {
        super(object);
        this.type = PunishmentType.valueOf(object.get("type").getAsString());
        this.uuid = UUID.fromString(object.get("uuid").getAsString());
        this.reason = object.get("reason").getAsString();
        this.executor = object.get("executor").getAsString();
    }

    @Override
    public JsonObject toJson() {
        return new JsonAppender(super.toJson())
                .append("type", this.type.name())
                .append("uuid", this.uuid.toString())
                .append("reason", this.reason)
                .append("executor", this.executor).get();
    }
}