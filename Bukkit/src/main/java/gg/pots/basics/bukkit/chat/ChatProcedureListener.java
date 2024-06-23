package gg.pots.basics.bukkit.chat;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@RequiredArgsConstructor
public class ChatProcedureListener implements Listener {

    private final ChatProcedureModule chatProcedureModule;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final String message = event.getMessage();

        for (ChatProcedure procedure : chatProcedureModule.getProcedures()) {
            if (procedure.shouldHandle(player)) {
                final ChatProcedureResult result = procedure.handle(player, message);

                if(result.isCancelled()) {
                    event.setCancelled(result.isCancelled());
                    return;
                }

                event.setFormat(result.getFormat());
            }
        }
    }
}