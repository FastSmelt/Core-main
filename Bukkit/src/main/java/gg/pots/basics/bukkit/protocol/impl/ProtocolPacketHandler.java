package gg.pots.basics.bukkit.protocol.impl;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import gg.pots.basics.bukkit.CoreSpigotPlugin;
import gg.pots.basics.bukkit.protocol.ProtocolHandler;

public class ProtocolPacketHandler extends ProtocolHandler {

    protected PacketAdapter packetAdapter;
    protected PacketAdapter sendPacketAdapter;

    @Override
    public void initializePacket() {
        this.packetAdapter = new PacketAdapter(CoreSpigotPlugin.instance, ListenerPriority.HIGHEST, PacketType.Play.Client.TAB_COMPLETE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType().equals(PacketType.Play.Client.TAB_COMPLETE)) {
                    if (event.getPlayer().hasPermission("core.admin.bypass")) {
                        return;
                    }
                    
                    final PacketContainer packet = event.getPacket();
                    final String message = packet.getSpecificModifier(String.class).read(0).toLowerCase();

                    if (message.startsWith("/") || message.startsWith("/ver") || message.startsWith("/version") || message.startsWith("/?") || message.startsWith("/about") || message.startsWith("/help")) {
                        event.setCancelled(true);
                    }
                }
            }
        };
        
        this.sendPacketAdapter = new PacketAdapter(CoreSpigotPlugin.instance, ListenerPriority.NORMAL, PacketType.Play.Server.TAB_COMPLETE) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Server.TAB_COMPLETE) {
                    if (!event.getPlayer().hasPermission("core.admin.bypass")) {
                        event.getPacket().getStringArrays().write(0, new String[0]);
                    }
                }
            }
        };

        ProtocolLibrary.getProtocolManager().addPacketListener(this.packetAdapter);
        ProtocolLibrary.getProtocolManager().addPacketListener(this.sendPacketAdapter);
    }
}
