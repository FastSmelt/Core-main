package gg.pots.basics.bukkit.command.teleport;

import io.github.nosequel.command.annotation.Command;
import io.github.nosequel.command.bukkit.executor.BukkitCommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeleportCommands {

    @Command(label = "telport", aliases = { "tp"}, permission = "core.teleport")
    public void teleport(BukkitCommandExecutor executor, Player target) {
        executor.getPlayer().getServer().getPlayer(executor.getPlayer().getName()).teleport(target);

        executor.sendMessage(ChatColor.YELLOW + "Teleported to " + target.getName() + ".");
    }

    @Command(label = "telporthere", aliases = { "tphere"}, permission = "core.teleport")
    public void teleportHere(BukkitCommandExecutor executor, Player target) {
        target.teleport(executor.getPlayer().getServer().getPlayer(executor.getPlayer().getName()));

        executor.sendMessage(ChatColor.YELLOW + "Teleported " + target.getName() + " to you.");
        target.sendMessage(ChatColor.YELLOW + "Teleported you by " + executor.getPlayer().getName() + ".");
    }
}
