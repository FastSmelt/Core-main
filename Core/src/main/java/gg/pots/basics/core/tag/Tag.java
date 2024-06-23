package gg.pots.basics.core.tag;

import com.google.gson.JsonObject;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.json.JsonAppender;
import lombok.Data;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Data
public class Tag {

    private final TagService tagService = ServiceHandler.getInstance().find(TagService.class);

    private final UUID uuid;

    private String name;
    private String displayName;
    private String prefix = "";
    private String permission = "";

    private ItemStack icon = new ItemStack(Material.ITEM_FRAME);

    /**
     * Constructor to create a new {@link Tag} object.
     *
     * @param uuid the unique identifier.
     * @param name the name.
     */

    public Tag(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.displayName = name;

        this.tagService.createTag(this);
    }

    /**
     * Constructor to create a new {@link Tag} object.
     *
     * @param object the json object.
     */

    public Tag(JsonObject object) {
        this.uuid = UUID.fromString(object.get("uuid").getAsString());
        this.name = object.get("name").getAsString();
        this.displayName = object.get("displayName").getAsString();
        this.prefix = object.get("prefix").getAsString();
        this.permission = object.get("permission").getAsString();
        this.icon = new ItemStack(Material.valueOf(object.get("icon").getAsString()));

        this.tagService.createTag(this);
    }

    /**
     * Get the {@link Tag} as a {@link JsonObject}
     *
     * @return the json object.
     */

    public JsonObject toJson() {
        return new JsonAppender()
                .append("uuid", this.uuid.toString())
                .append("name", this.name)
                .append("displayName", this.displayName)
                .append("prefix", this.prefix)
                .append("permission", this.permission)
                .append("icon", this.icon.getType().name()).get();
    }

    /**
     * Get the prefix of the {@link Tag}
     *
     * @return the prefix.
     */

    public String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&', this.prefix.replace("_", " "));
    }
}
