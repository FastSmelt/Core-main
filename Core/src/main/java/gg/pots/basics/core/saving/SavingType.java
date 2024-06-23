package gg.pots.basics.core.saving;

import com.google.gson.JsonObject;
import gg.pots.basics.core.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface SavingType extends Service {

    @Override
    default void load() {}

    @Override
    default void unload() {}

    List<JsonObject> getJsonObjects(String collection);

    Optional<JsonObject> getJsonObject(UUID uuid, String collection);

    boolean saveJsonObject(JsonObject object, String collection);

    void saveJsonObjectAsync(JsonObject object, String collection);

    CompletableFuture<Boolean> deleteFromCollection(UUID uuid, String collection);
}