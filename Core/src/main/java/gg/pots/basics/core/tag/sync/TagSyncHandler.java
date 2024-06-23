package gg.pots.basics.core.tag.sync;

import com.google.gson.JsonObject;
import gg.pots.basics.core.saving.sync.SyncHandler;
import gg.pots.basics.core.tag.Tag;
import gg.pots.basics.core.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@RequiredArgsConstructor
public class TagSyncHandler implements SyncHandler {

    private final TagService tagService;

    @Override
    public void incoming(String channel, JsonObject object) {
        if (channel.equals(this.getChannel())) {
            final UUID tagUuid = UUID.fromString(object.get("uuid").getAsString());
            final String tagName = object.get("name").getAsString();
            final Tag tag = this.tagService.findOrMake(tagUuid, tagName);

            tag.setName(object.get("name").getAsString());
            tag.setDisplayName(object.get("displayName").getAsString());
            tag.setPrefix(object.get("prefix").getAsString());
            tag.setPermission(object.get("permission").getAsString());
            tag.setIcon(new ItemStack(Material.valueOf(object.get("icon").getAsString())));

            // Convert ItemStack to String representation of Material enum
            object.addProperty("icon", tag.getIcon().getType().name());
        }
    }

    @Override
    public String getChannel() {
        return "tags";
    }
}
