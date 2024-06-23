package gg.pots.basics.core.expirable;

import com.google.gson.JsonObject;
import gg.pots.basics.core.json.JsonAppender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Expirable {

    private final long startEpoch;

    private long expirationEpoch;
    private long expirationDateEpoch = -1L;
    private boolean active;

    /**
     * Constructor for making a new instance of a {@link Expirable} object
     *
     * @param startEpoch      the epoch when the expirable object was created
     * @param expirationEpoch the epoch when the expirable object will be expired
     * @param active          whether the expirable object is active
     */

    public Expirable(long startEpoch, long expirationEpoch, boolean active) {
        this.startEpoch = startEpoch;
        this.expirationEpoch = expirationEpoch;
        this.active = active;
    }

    /**
     * Constructor for deserializing an {@link Expirable} object from a {@link JsonObject}
     *
     * @param object the json object
     */

    public Expirable(JsonObject object) {
        this.startEpoch = object.get("startEpoch").getAsLong();
        this.expirationEpoch = object.get("expirationEpoch").getAsLong();
        this.active = object.get("active").getAsBoolean();
    }

    /**
     * Check if the punishment is active
     *
     * @return whether the punishment is active or not
     */

    public boolean isActive() {
        if (this.getExpirationEpoch() != -1L && this.getExpirationEpoch() - System.currentTimeMillis() <= 0) {
            this.setActive(false);
        }

        return this.active;
    }

    /**
     * Set the active state of an {@link Expirable} object
     *
     * @param active the active state
     */

    public void setActive(boolean active) {
        if (!active) {
            this.setExpirationDateEpoch(System.currentTimeMillis());
        }

        this.active = active;
    }

    /**
     * Serialize a {@link Expirable} to a {@link JsonObject}
     *
     * @return the serialized json object
     */

    public JsonObject toJson() {
        return new JsonAppender()
                .append("startEpoch", this.startEpoch)
                .append("expirationEpoch", this.expirationEpoch)
                .append("active", this.active).get();
    }
}