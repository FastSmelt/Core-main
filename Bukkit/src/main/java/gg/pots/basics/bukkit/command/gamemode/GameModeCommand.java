package gg.pots.basics.bukkit.command.gamemode;

import gg.pots.basics.bukkit.util.CC;
import io.github.nosequel.command.annotation.Command;
import io.github.nosequel.command.bukkit.executor.BukkitCommandExecutor;
import org.bukkit.GameMode;

public class GameModeCommand {

    @Command(label = "gamemode", aliases = "gm", permission = "core.gamemode", userOnly = true)
    public void gameMode(BukkitCommandExecutor executor, GameMode gameMode) {
        executor.getPlayer().setGameMode(gameMode);

        executor.sendMessage(CC.translate("&eYour gamemode has been set to &a" + gameMode.name().toLowerCase() + "&e."));
    }
}