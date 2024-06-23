package gg.pots.basics.bukkit.command.fly;

import gg.pots.basics.bukkit.util.CC;
import io.github.nosequel.command.annotation.Command;
import io.github.nosequel.command.bukkit.executor.BukkitCommandExecutor;

public class FlyCommand {

    @Command(label = "fly", permission = "core.fly", userOnly = true)
    public void fly(BukkitCommandExecutor executor) {
        executor.getPlayer().setAllowFlight(!executor.getPlayer().getAllowFlight());

        executor.sendMessage(CC.translate(
                "&fYou have " + (executor.getPlayer().getAllowFlight() ? "&aenabled" : "&cdisabled") + " &fyour flight."
        ));
    }
}