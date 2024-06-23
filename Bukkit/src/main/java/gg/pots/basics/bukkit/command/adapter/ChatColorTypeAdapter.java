package gg.pots.basics.bukkit.command.adapter;

import io.github.nosequel.command.adapter.TypeAdapter;
import io.github.nosequel.command.executor.CommandExecutor;
import org.bukkit.ChatColor;

public class ChatColorTypeAdapter implements TypeAdapter<ChatColor> {

    @Override
    public ChatColor convert(CommandExecutor commandExecutor, String source) {
        return ChatColor.valueOf(source);
    }
}