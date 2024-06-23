package gg.pots.basics.bungee;

import gg.pots.basics.bungee.command.HubCommand;
import gg.pots.basics.bungee.command.JoinCommand;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public class CoreBungeePlugin extends Plugin {

    public static CoreBungeePlugin instance;

    @Override
    public void onEnable() {
        instance =  this;

        this.getProxy().getPluginManager().registerCommand(this, new HubCommand());
        this.getProxy().getPluginManager().registerCommand(this, new JoinCommand("join"));
    }

    @Override
    public void onDisable() {
        instance = null;
    }
}
