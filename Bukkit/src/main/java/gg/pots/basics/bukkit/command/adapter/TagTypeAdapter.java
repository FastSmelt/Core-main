package gg.pots.basics.bukkit.command.adapter;

import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.tag.Tag;
import gg.pots.basics.core.tag.TagService;
import io.github.nosequel.command.adapter.TypeAdapter;
import io.github.nosequel.command.executor.CommandExecutor;

public class TagTypeAdapter implements TypeAdapter<Tag> {

    private final TagService tagService = ServiceHandler.getInstance().find(TagService.class);

    @Override
    public Tag convert(CommandExecutor commandExecutor, String source) {
        return this.tagService.find(source);
    }
}
