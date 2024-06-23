package gg.pots.basics.bungee.command;

import gg.pots.basics.bungee.CoreBungeePlugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class HubCommand extends Command {

    public HubCommand() {
        super("hub", "", "lobby");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            return;
        }

        final ProxiedPlayer proxiedPlayer = (ProxiedPlayer) commandSender;

        if (!proxiedPlayer.getServer().equals("lobby")) {
            proxiedPlayer.sendMessage(ChatColor.GREEN + "Sending you to the lobby...");
            proxiedPlayer.connect(CoreBungeePlugin.instance.getProxy().getServerInfo("lobby"));
        } else {
            proxiedPlayer.sendMessage(ChatColor.RED + "You are already connected to the lobby!");
        }
    }
}
