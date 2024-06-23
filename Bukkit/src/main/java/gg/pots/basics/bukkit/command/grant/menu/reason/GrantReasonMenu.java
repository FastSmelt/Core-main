package gg.pots.basics.bukkit.command.grant.menu.reason;

import gg.pots.basics.bukkit.command.grant.menu.BaseHeaderMenu;
import gg.pots.basics.bukkit.command.grant.menu.duration.GrantDurationMenu;
import gg.pots.basics.bukkit.util.CC;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.expirable.grant.Grant;
import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.profile.ProfileService;
import gg.pots.basics.core.rank.Rank;
import gg.pots.basics.core.util.DurationUtil;
import io.github.nosequel.menu.Menu;
import io.github.nosequel.menu.buttons.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;
import java.util.function.Consumer;

public class GrantReasonMenu extends BaseHeaderMenu {

    private String reason;

    private final Rank rank;
    private final long duration;

    private final CoreProfile target;

    public GrantReasonMenu(Player player, CoreProfile target, Rank rank, long duration) {
        super(player, "Select a reason", 18);

        this.target = target;
        this.rank = rank;
        this.duration = duration;
    }

    @Override
    public void tick() {
        super.tick();

        for (int i = 0; i < GrantReasonType.values().length; i++) {
            final GrantReasonType reasonType = GrantReasonType.values()[i];

            final Consumer<InventoryClickEvent> action = event -> {
                event.setCancelled(true);

                this.reason = reasonType.getDisplay();
                this.updateMenu();
            };

            this.buttons[i + 9] = new Button(Material.WATCH)
                    .setDisplayName(CC.translate("&6" + reasonType.getDisplay()))
                    .setLore(this.getLore(reasonType))
                    .setClickAction(action);
        }
    }

    private String[] getLore(GrantReasonType reasonType) {
        return new String[] {
                CC.translate("&eClick to set the reason to &6" + reasonType.getDisplay() + "&e."),
        };
    }

    @Override
    public Consumer<InventoryClickEvent> getAcceptAction() {
        return event -> {
            event.setCancelled(true);

            final ProfileService profileService = ServiceHandler.getInstance().find(ProfileService.class);

            profileService.find(this.target.getUuid()).getGrants().add(
                    new Grant(
                            UUID.randomUUID(),
                            this.rank,
                            this.getPlayer().getName(),
                            this.reason,
                            this.duration
                    )
            );

            profileService.syncProfile(target);

            if (this.target.getPlayer().isOnline()) {
                if (duration == -1L) {
                    this.getPlayer().sendMessage(CC.translate("&aYou have been granted the rank "
                            + rank.getDisplayName() + " &apermanently&a.")
                    );
                } else {
                    this.getPlayer().sendMessage(CC.translate("&aYou have been granted the rank "
                            + rank.getDisplayName() + " &afor &e" + DurationUtil.millisToRoundedTime(duration) + "&a.")
                    );
                }
            }

            this.getPlayer().sendMessage(CC.translate("&aSuccessfully granted "
                    + this.target.getColoredName()
                    + " &athe rank " + this.rank.getDisplayName() + "&a.")
            );

            this.getPlayer().closeInventory();
        };
    }

    @Override
    public Consumer<InventoryClickEvent> getCancelAction() {
        return event -> {
            event.setCancelled(true);

            this.getPlayer().closeInventory();
        };
    }

    @Override
    public String getSecondDisplay() {
        return CC.translate("&cCancel");
    }

    @Override
    public Menu getPreviousMenu() {
        return new GrantDurationMenu(this.getPlayer(), this.target, this.rank);
    }
}