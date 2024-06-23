package gg.pots.basics.core.saving;

import com.google.gson.JsonObject;
import gg.pots.basics.core.saving.sync.SyncHandler;

import java.util.List;

public interface SyncType {

    void publish(String channel, JsonObject object);

    void incoming(String message);

    void registerHandler(SyncHandler handler);

    List<SyncHandler> getSyncHandlers();
}
