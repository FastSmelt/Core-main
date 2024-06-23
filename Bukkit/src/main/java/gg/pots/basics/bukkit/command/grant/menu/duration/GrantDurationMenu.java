package gg.pots.basics.bukkit.command.grant.menu.duration;

import gg.pots.basics.bukkit.CoreSpigotPlugin;
import gg.pots.basics.bukkit.command.grant.menu.BaseHeaderMenu;
import gg.pots.basics.bukkit.command.grant.menu.GrantMenu;
import gg.pots.basics.bukkit.command.grant.menu.reason.GrantReasonMenu;
import gg.pots.basics.bukkit.util.CC;
import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.rank.Rank;
import gg.pots.basics.core.rank.RankService;
import io.github.nosequel.menu.Menu;
import io.github.nosequel.menu.buttons.Button;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

@Getter
public class GrantDurationMenu extends BaseHeaderMenu {

    private long currentDuration;

    private final CoreProfile target;
    private final Rank rank;

    public GrantDurationMenu(Player player, CoreProfile target, Rank rank) {
        super(player, "Grant Duration", 18);

        this.target = target;
        this.rank = rank;
    }

    @Override
    public void tick() {
        super.tick();

        for (int i = 0; i < GrantDurationType.values().length; i++) {
            final GrantDurationType durationType = GrantDurationType.values()[i];

            final Consumer<InventoryClickEvent> action = event -> {
                event.setCancelled(true);

                this.currentDuration += durationType.getDuration();
                this.updateMenu();
            };

            this.buttons[i + 9] = new Button(Material.WATCH)
                    .setDisplayName(CC.translate("&6" + durationType.getDisplay()))
                    .setLore(this.getLore())
                    .setClickAction(action);
        }
    }

    private String[] getLore() {
        final Date date = new Date(System.currentTimeMillis() + this.currentDuration);
        final DateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        return new String[] {
                CC.translate("&7( Left | Right) "),
                CC.translate("&a&l +1 &c&l -1 "),
                "",
                CC.translate("&eCurrent duration will last until &6" + format.format(date) + "&e.")
        };
    }

    @Override
    public Consumer<InventoryClickEvent> getAcceptAction() {
        return event -> {
            event.setCancelled(true);
            event.getWhoClicked().closeInventory();

            new GrantReasonMenu(this.getPlayer(), this.target, this.rank, this.currentDuration).updateMenu();
        };
    }

    @Override
    public Consumer<InventoryClickEvent> getCancelAction() {
        return event -> {
            event.setCancelled(true);

            this.getPlayer().closeInventory();
            this.currentDuration = -1L;

            new GrantReasonMenu(this.getPlayer(), this.target, this.rank, this.currentDuration).updateMenu();
        };
    }

    @Override
    public String getSecondDisplay() {
        return CC.translate("&cPermanent");
    }

    @Override
    public Menu getPreviousMenu() {
        return new GrantMenu(this.getPlayer(), this.target, CoreSpigotPlugin.instance.getHandler().find(RankService.class));
    }
}
