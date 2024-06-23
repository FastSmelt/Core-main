package gg.pots.basics.bukkit.command.history.menu;

import gg.pots.basics.bukkit.util.CC;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.expirable.punishment.Punishment;
import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.profile.ProfileService;
import gg.pots.basics.core.util.DurationUtil;
import io.github.nosequel.menu.buttons.Button;
import io.github.nosequel.menu.pagination.PaginatedMenu;
import lombok.Getter;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
public class HistoryMenu extends PaginatedMenu {

    private final ProfileService profileService = ServiceHandler.getInstance().find(ProfileService.class);

    private final CoreProfile target;

    public HistoryMenu(Player player, CoreProfile target) {
        super(player, CC.translate("&eHistory of " + target.getColoredName()), 27);

        this.target = target;

        this.setPreviousPageButton(new Button(Material.CARPET)
                .setDisplayName(CC.translate("&cPrevious Page"))
                .setData(DyeColor.RED.getWoolData()));

        this.setNextPageButton(new Button(Material.CARPET)
                .setDisplayName(CC.translate("&aNext Page"))
                .setData(DyeColor.GREEN.getWoolData()));
    }

    @Override
    public void tick() {
        final List<Punishment> punishments = new ArrayList<>(this.profileService
                .find(target.getUuid())
                .getPunishments());

        for (int i = 0; i < punishments.size(); i++) {
            final Punishment punishment = punishments.get(i);

            this.buttons[i] = new Button(Material.WOOL)
                    .setDisplayName((punishment.isActive() ? CC.translate("&a(Active)") : CC.translate("&c(Inactive)")) + " &8#" + punishment.getUuid().toString().substring(0, 8))
                    .setLore(this.getLore(punishment))
                    .setData(punishment.isActive() ? DyeColor.LIME.getWoolData() : DyeColor.RED.getWoolData())
                    .setClickAction(action -> {
                        action.setCancelled(true);
                    });
        }
    }

    private String[] getLore(Punishment punishment) {
        return new String[] {
                "",
                CC.translate("&7Issued: &a" + new Date(punishment.getStartEpoch())),
                CC.translate("&7Issued By: &f" + punishment.getExecutor()),
                CC.translate("&7Reason: &f" + punishment.getReason()),
                "",
                CC.translate("&7Duration: &f" + (punishment.isActive() ? punishment.getExpirationEpoch() == -1L ? "Never" : DurationUtil.millisToRoundedTime(punishment.getExpirationEpoch() - System.currentTimeMillis()) : DurationUtil.unixToDate(punishment.getExpirationEpoch())))
        };
    }
}