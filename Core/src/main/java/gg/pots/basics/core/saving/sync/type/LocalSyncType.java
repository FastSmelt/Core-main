package gg.pots.basics.core.saving.sync.type;

import com.google.gson.JsonObject;
import gg.pots.basics.core.saving.sync.SyncHandler;
import gg.pots.basics.core.json.JsonAppender;
import gg.pots.basics.core.json.JsonUtils;
import gg.pots.basics.core.saving.SyncType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LocalSyncType implements SyncType {

    private final List<SyncHandler> syncHandlers = new ArrayList<>();

    @Override
    public void publish(String channel, JsonObject object) {
        this.incoming(new JsonAppender(object).append("channel", channel).toString());
    }

    @Override
    public void incoming(String message) {
        final JsonObject object = JsonUtils.getParser().parse(message).getAsJsonObject();
        final String channel = object.get("channel").getAsString();

        this.syncHandlers.stream()
                .filter(handler -> handler.getChannel().equals(channel))
                .forEach(handler -> handler.incoming(channel, object));
    }

    @Override
    public void registerHandler(SyncHandler handler) {
        this.syncHandlers.add(handler);
    }
}