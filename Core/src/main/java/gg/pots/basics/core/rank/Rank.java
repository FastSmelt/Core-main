package gg.pots.basics.core.rank;

import com.google.gson.JsonObject;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.json.JsonAppender;
import gg.pots.basics.core.json.JsonUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class Rank {

    private final RankService rankService = ServiceHandler.getInstance().find(RankService.class);

    private final Set<String> permissions = new HashSet<>();
    private final Set<UUID> inherits = new HashSet<>();

    private final UUID uuid;

    private String name;
    private String displayName;
    private String prefix = "";

    private ChatColor color;

    private int weight;

    private boolean defaultRank;
    private boolean hidden;

    /**
     * Constructor for making a new {@link Rank} object
     *
     * @param uuid the unique identifier of the rank
     * @param name the displayed name
     */

    public Rank(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.displayName = name;
        this.color = ChatColor.WHITE;

        this.rankService.registerRank(this);
    }

    /**
     * Constructor for making a new {@link Rank} object with a default {@link Boolean} value for the state
     * of the default rank value
     *
     * @param uuid        the unique identifier of the rank
     * @param name        the displayed name
     * @param defaultRank the state of default of the rank
     */

    public Rank(UUID uuid, String name, boolean defaultRank) {
        this(uuid, name);
        this.defaultRank = defaultRank;
    }

    /**
     * Set the weight of the {@link Rank}
     * This will automatically sort the list of ranks
     *
     * @param weight the new weight
     */

    public void setWeight(int weight) {
        this.weight = weight;
        this.rankService.sort(Comparator.comparingInt(Rank::getWeight).reversed());
    }

    /**
     * Constructor for loading a {@link Rank} from a {@link JsonObject}
     *
     * @param object the json object to load the rank from
     */

    public Rank(JsonObject object) {
        this.uuid = UUID.fromString(object.get("uuid").getAsString());
        this.name = object.get("name").getAsString();
        this.displayName = object.get("displayName").getAsString();
        this.weight = object.get("weight").getAsInt();
        this.prefix = object.get("prefix").getAsString();
        this.defaultRank = object.get("defaultRank").getAsBoolean();
        this.hidden = object.get("hidden").getAsBoolean();
        this.color = ChatColor.valueOf(object.get("color").getAsString());

        JsonUtils.getParser().parse(object.get("permissions").getAsString())
                .getAsJsonArray().forEach(element -> this.permissions.add(element.getAsString()));

        if (object.has("inherits")) {
            JsonUtils.getParser().parse(object.get("inherits").getAsString())
                    .getAsJsonArray().forEach(element -> this.inherits.add(UUID.fromString(element.getAsString())));
        }

        this.rankService.registerRank(this);
    }

    /**
     * Method to get all the permissions of a rank including inherit permissions
     *
     * @return the set of permissions
     */

    public Collection<String> getAllPermissions() {
        final Set<String> permissions = new HashSet<>(this.permissions);
        this.inherits.forEach(inherit -> permissions.addAll(this.rankService.find(inherit).getAllPermissions()));

        return permissions;
    }

    /**
     * Add a permission to the {@link Rank} object
     *
     * @param permission the permission
     */

    public void addPermission(String permission) {
        this.permissions.add(permission);
    }

    /**
     * Remove a permission from a {@link Rank} object
     *
     * @param permission the permission to remove
     */

    public boolean removePermission(String permission) {
        return this.permissions.remove(permission);
    }

    /**
     * Get the prefix of a {@link Rank} object
     *
     * @return the prefix.
     */

    public String getPrefix() {
        return this.prefix.replace("_", " ");
    }

    /**
     * Serialize a {@link Rank} to a {@link JsonObject}
     *
     * @return the serialized json object
     */

    public JsonObject toJson() {
        return new JsonAppender()
                .append("uuid", this.uuid.toString())
                .append("name", this.name)
                .append("displayName", this.displayName)
                .append("weight", this.weight)
                .append("prefix", this.prefix)
                .append("permissions", this.permissions.toString())
                .append("inherits", this.inherits.stream().map(UUID::toString).collect(Collectors.toList()).toString())
                .append("defaultRank", this.defaultRank)
                .append("hidden", this.hidden)
                .append("color", this.color).get();
    }
}