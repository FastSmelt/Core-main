package gg.pots.basics.bungee.listener;

import gg.pots.basics.bungee.CoreBungeePlugin;
import gg.pots.basics.bungee.util.CC;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener {

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {
        if (event.getFrom() == null) {
            if (event.getPlayer().hasPermission("core.staff")) {
                for (ProxiedPlayer proxiedPlayer : CoreBungeePlugin.instance.getProxy().getPlayers()) {
                    if (proxiedPlayer.hasPermission("core.staff")) {
                        proxiedPlayer.sendMessage(CC.translate("&9[Staff] " + event.getPlayer().getName() + " &ajoined &b(" + event.getPlayer().getServer().getInfo().getName() + ")"));
                    }
                }
            }
        }

        if (event.getPlayer().hasPermission("core.staff")) {
            for (ProxiedPlayer proxiedPlayer : CoreBungeePlugin.instance.getProxy().getPlayers()) {
                if (proxiedPlayer.hasPermission("core.staff")) {
                    proxiedPlayer.sendMessage(CC.translate("&9[Staff] " + event.getPlayer().getName() + " &ajoined &b(" + event.getPlayer().getServer().getInfo().getName() + ") &bfrom (" + event.getFrom().getName() + ")"));
                }
            }
        }
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        if (event.getPlayer().hasPermission("core.staff")) {
            for (ProxiedPlayer proxiedPlayer : CoreBungeePlugin.instance.getProxy().getPlayers()) {
                if (proxiedPlayer.hasPermission("core.staff")) {
                    proxiedPlayer.sendMessage(CC.translate("&9[Staff] " + event.getPlayer().getName() + " &cleft &b(" + event.getPlayer().getServer().getInfo().getName() + ")"));
                }
            }
        }
    }
}
