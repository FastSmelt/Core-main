package gg.pots.basics.bukkit.command.grant.menu;

import io.github.nosequel.menu.Menu;
import io.github.nosequel.menu.buttons.Button;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

public abstract class BaseHeaderMenu extends Menu {

    public BaseHeaderMenu(Player player, String title, int size) {
        super(player, title, size);
    }

    @Override
    public void tick() {
        for (int i = 0; i < 9; i++) {
            this.buttons[i] = new Button(this.getFillerType());
        }

        this.buttons[2] = new Button(Material.INK_SACK)
                .setData(DyeColor.GREEN.getDyeData())
                .setDisplayName(ChatColor.GREEN + "Confirm")
                .setClickAction(this.getAcceptAction());

        this.buttons[4] = new Button(Material.LEVER)
                .setDisplayName(ChatColor.GREEN + "Return")
                .setClickAction(event -> {
                    event.setCancelled(true);
                    this.getPreviousMenu().updateMenu();
                });

        this.buttons[6] = new Button(Material.INK_SACK)
                .setData(DyeColor.RED.getDyeData())
                .setDisplayName(this.getSecondDisplay())
                .setClickAction(this.getCancelAction());
    }

    public abstract Consumer<InventoryClickEvent> getAcceptAction();

    public abstract Consumer<InventoryClickEvent> getCancelAction();

    public abstract String getSecondDisplay();

    public abstract Menu getPreviousMenu();
}