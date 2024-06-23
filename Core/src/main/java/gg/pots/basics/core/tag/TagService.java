package gg.pots.basics.core.tag;

import gg.pots.basics.core.Service;
import gg.pots.basics.core.json.JsonAppender;
import gg.pots.basics.core.saving.SavingService;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Getter
public class TagService implements Service {

    private final List<Tag> tags = new ArrayList<>();
    private final SavingService savingService;

    /**
     * Constructor to create a new {@link TagService}
     *
     * @param savingService the savingService.
     */

    public TagService(SavingService savingService) {
        this.savingService = savingService;
    }

    @Override
    public void load() {
        this.savingService.getSavingType().getJsonObjects("tags").forEach(Tag::new);
    }

    @Override
    public void unload() {
        this.stream().forEach(tag -> this.savingService.getSavingType().saveJsonObject(tag.toJson(), "tags"));
    }

    /**
     * Create a new {@link Tag}
     *
     * @param tag the tag.
     */

    public void createTag(Tag tag) {
        if (tag == null) {
            throw new IllegalArgumentException("The provided tag to create is null");
        }

        this.tags.add(tag);
    }

    /**
     * Delete a {@link Tag}
     *
     * @param tag the tag.
     */

    public void deleteTag(Tag tag) {
        if (tag == null) {
            throw new IllegalArgumentException("The provided tag to delete is null");
        }

        this.tags.remove(tag);
        this.savingService.getSyncType().publish("tag-delete", new JsonAppender().append("uuid", tag.getUuid().toString()).get());

        this.savingService.getSavingType().deleteFromCollection(tag.getUuid(), "tags");
    }

    /**
     * Sync a {@link Tag}
     *
     * @param tag the tag.
     */

    public void syncTag(Tag tag) {
        this.savingService.getSyncType().publish("tags", tag.toJson());
    }

    /**
     * Stream the current {@link TagService} object.
     *
     * @return the stream of tags.
     */

    public Stream<Tag> stream() {
        return this.tags.stream();
    }

    /**
     * Find a {@link Tag} by a {@link String}
     *
     * @param name the name.
     * @return the tag or null.
     */

    public Tag find(String name) {
        return this.tags.stream()
                .filter(current -> current.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    /**
     * Find a {@link Tag} by a {@link UUID}
     *
     * @param uuid the unique identifier.
     * @return the tag or null.
     */

    public Tag find(UUID uuid) {
        return this.tags.stream()
                .filter(current -> current.getUuid().equals(uuid))
                .findFirst().orElse(null);
    }

    /**
     * Find a {@link Tag} by a {@link UUID} and make a new tag if the tag could not be found.
     *
     * @param tagUuid the unique identifier of the tag.
     * @param tagName the name of the tag.
     * @return the tag.
     */

    public Tag findOrMake(UUID tagUuid, String tagName) {
        return this.tags.stream()
                .filter(current -> current.getUuid().equals(tagUuid))
                .findFirst().orElseGet(() -> new Tag(tagUuid, tagName));
    }
}
