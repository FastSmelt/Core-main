package gg.pots.basics.bukkit.protocol;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import lombok.Getter;

@Getter
public abstract class ProtocolHandler {

    protected ProtocolManager protocolManager;

    public ProtocolHandler() {
        this.protocolManager = ProtocolLibrary.getProtocolManager();
    }

    /**
     * Initializes the ProtocolLib packet system
     */
    public abstract void initializePacket();
}
