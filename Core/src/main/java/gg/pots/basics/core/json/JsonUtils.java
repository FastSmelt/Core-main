package gg.pots.basics.core.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.lang.Nullable;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class JsonUtils {

    @Getter
    private final JsonParser parser = new JsonParser();

    /**
     * Get a {@link JsonObject} from a {@link String}
     *
     * @param string the string to get the json object from
     * @return the parsed JsonObject
     */

    public JsonObject getJsonFromString(String string) {
        if (string.isEmpty()) {
            throw new IllegalArgumentException("String could not be parsed because it's empty");
        }

        return parser.parse(string).getAsJsonObject();
    }

    /**
     * Get a {@link JsonObject} from a {@link Map<String,String>}
     *
     * @param map the map to get the json object from
     * @return the json object
     */

    public JsonObject getFromMap(Map<String, String> map) {
        return new JsonAppender() {{
            map.forEach(this::append);
        }}.get();
    }

    /**
     * Get a {@link JsonObject} from a {@link Document}
     *
     * @param document the document to get the json object from
     * @return the json object
     */

    public JsonObject getFromDocument(@Nullable Document document) {
        if (document == null) {
            return null;
        }

        return new JsonAppender() {{
            document.forEach((str, obj) -> {
                if(obj instanceof String) {
                    append(str, (String) obj);
                }
            });
        }}.get();
    }

    /**
     * Get a {@link Document} from a {@link JsonObject}
     *
     * @param object the object to get the document from
     * @return the document
     */

    public Document toDocument(JsonObject object) {
        if (object == null) {
            return new Document();
        }

        return new Document() {{
            object.entrySet().forEach(entry -> put(entry.getKey(), entry.getValue().getAsString()));
        }};
    }

    /**
     * Get a {@link Map} from a {@link JsonObject}
     *
     * @param object the {@link JsonObject} to get the {@link Map} from
     * @return the {@link Map}
     */

    public Map<String, JsonElement> getMap(JsonObject object) {
        return new HashMap<String, JsonElement>() {{
            object.entrySet().forEach(entry -> put(entry.getKey(), entry.getValue()));
        }};
    }
}