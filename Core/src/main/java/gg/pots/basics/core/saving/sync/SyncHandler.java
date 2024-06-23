package gg.pots.basics.core.saving.sync;

import com.google.gson.JsonObject;

public interface SyncHandler {

    /**
     * Handle an incoming {@link JsonObject} message
     *
     * @param channel the channel where the object was sent in
     * @param object  the object which was sent
     */
    void incoming(String channel, JsonObject object);

    /**
     * The handleable channels which this handler will handle
     *
     * @return the channels
     */
    String getChannel();
}
