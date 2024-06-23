package gg.pots.basics.core.tag.sync;

import com.google.gson.JsonObject;
import gg.pots.basics.core.saving.sync.SyncHandler;
import gg.pots.basics.core.tag.Tag;
import gg.pots.basics.core.tag.TagService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class TagDeleteHandler implements SyncHandler {

    private final TagService tagService;

    @Override
    public void incoming(String channel, JsonObject object) {
        if (channel.equals("tag-delete")) {
            final UUID tagUuid = UUID.fromString(object.get("uuid").getAsString());
            final Tag tag = this.tagService.find(tagUuid);

            if (tag != null) {
                this.tagService.deleteTag(tag);
            }
        }
    }

    @Override
    public String getChannel() {
        return "tag-delete";
    }
}
