package gg.pots.basics.bungee.command;

import gg.pots.basics.bungee.CoreBungeePlugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class JoinCommand extends Command {

    public JoinCommand(String name) {
        super("join", "core.admin");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            return;
        }

        if (strings.length == 0) {
            commandSender.sendMessage(ChatColor.RED + "Usage: /join <server>");
            return;
        }

        if (strings.length > 0) {
            String serverName = strings[0];

            final ServerInfo serverInfo = CoreBungeePlugin.instance.getProxy().getServerInfo(serverName);

            if (serverInfo == null) {
                commandSender.sendMessage(ChatColor.RED + "Server not found!");
                return;
            }

            if (serverInfo != null) {
                final ProxiedPlayer proxiedPlayer = (ProxiedPlayer) commandSender;
                proxiedPlayer.sendMessage(ChatColor.GREEN + "Sending you to " + serverName + "...");
                proxiedPlayer.connect(serverInfo);
            } else {
                commandSender.sendMessage(ChatColor.RED + "You are already connected to " + serverName + "!");
            }
        }
    }
}
