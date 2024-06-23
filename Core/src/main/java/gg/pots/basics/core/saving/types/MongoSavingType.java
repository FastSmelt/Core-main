package gg.pots.basics.core.saving.types;

import com.google.gson.JsonObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import gg.pots.basics.core.json.JsonUtils;
import gg.pots.basics.core.saving.SavingType;
import lombok.Getter;
import org.bson.Document;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Getter
public class MongoSavingType implements SavingType {

    private final MongoDatabase database;

    /**
     * Constructor for making a new {@link MongoSavingType} without authentication
     *
     * @param hostname the hostname of the server
     * @param port     the port of the server
     * @param database the database to gather the data from
     */

    public MongoSavingType(String hostname, int port, String database) {
        this.database = new MongoClient(hostname, port).getDatabase(database);
    }

    @Override
    public List<JsonObject> getJsonObjects(String collection) {
        final List<JsonObject> objects = new ArrayList<>();
        this.database.getCollection(collection).find().forEach((Block<? super Document>) document -> objects.add(JsonUtils.getFromDocument(document)));

        return objects;
    }

    @Override
    public Optional<JsonObject> getJsonObject(UUID uuid, String collection) {
        final JsonObject object = JsonUtils.getFromDocument(this.database.getCollection(collection).find(Filters.eq("uuid", uuid.toString())).first());

        return object == null ? Optional.empty() : Optional.of(object);
    }

    @Override
    public boolean saveJsonObject(JsonObject object, String collectionName) {
        if (!object.has("uuid")) {
            return false;
        }

        final Document document = JsonUtils.toDocument(object);
        final UUID uuid = UUID.fromString(document.getString("uuid"));

        return this.database.getCollection(collectionName).replaceOne(
                Filters.eq("uuid", uuid.toString()),
                document,
                new ReplaceOptions().upsert(true)
        ).wasAcknowledged();
    }

    @Override
    public void saveJsonObjectAsync(JsonObject object, String collection) {
        try {
            CompletableFuture.runAsync(() -> {
                if (!object.has("uuid")) {
                    return;
                }

                final Document document = JsonUtils.toDocument(object);
                final UUID uuid = UUID.fromString(document.getString("uuid"));

                database.getCollection(collection).replaceOne(
                        Filters.eq("uuid", uuid.toString()),
                        document,
                        new ReplaceOptions().upsert(true));
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public CompletableFuture<Boolean> deleteFromCollection(UUID uuid, String collection) {
        return CompletableFuture.supplyAsync(() -> {
            final DeleteResult result = this.database.getCollection(collection).deleteOne(Filters.eq("uuid", uuid.toString()));
            return result.wasAcknowledged();
        });
    }
}