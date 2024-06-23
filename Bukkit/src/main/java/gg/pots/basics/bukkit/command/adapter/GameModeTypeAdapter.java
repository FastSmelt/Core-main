package gg.pots.basics.bukkit.command.adapter;

import io.github.nosequel.command.adapter.TypeAdapter;
import io.github.nosequel.command.executor.CommandExecutor;
import org.bukkit.GameMode;

import java.util.Arrays;

public class GameModeTypeAdapter implements TypeAdapter<GameMode> {

    @Override
    public GameMode convert(CommandExecutor commandExecutor, String source) {
        return Arrays.stream(GameMode.values())
                .filter(gameMode -> gameMode.name().startsWith(source.toUpperCase()))
                .findFirst().orElseGet(() -> GameMode.getByValue(Integer.parseInt(source)));
    }
}
