package gg.pots.basics.bukkit.chat;

import org.bukkit.entity.Player;

public interface ChatProcedure {

    /**
     * Handle a message from a player
     *
     * @param player  the player who sent the message
     * @param message the message which had been sent
     * @return the result of the handling
     */
    ChatProcedureResult handle(Player player, String message);

    /**
     * Check if the player should be handled by this {@link ChatProcedure}
     *
     * @param player the player to check
     * @return whether they should be handled
     */
    boolean shouldHandle(Player player);

    /**
     * Get the weight of the {@link ChatProcedure}
     *
     * @return the {@link ChatProcedureWeight}
     */
    ChatProcedureWeight getWeight();

    /**
     * Get the weight of the {@link ChatProcedure}
     *
     * @return the numeric weight
     */
    default int getNumericWeight() {
        return this.getWeight().getWeight();
    }
}
