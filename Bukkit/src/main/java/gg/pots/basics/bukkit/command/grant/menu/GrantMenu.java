package gg.pots.basics.bukkit.command.grant.menu;

import gg.pots.basics.bukkit.command.grant.menu.duration.GrantDurationMenu;
import gg.pots.basics.bukkit.util.CC;
import gg.pots.basics.bukkit.util.color.ColorUtility;
import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.rank.Rank;
import gg.pots.basics.core.rank.RankService;
import io.github.nosequel.menu.buttons.Button;
import io.github.nosequel.menu.pagination.PaginatedMenu;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Getter
public class GrantMenu extends PaginatedMenu {

    private final RankService rankService;
    private final CoreProfile target;

    public GrantMenu(Player player, CoreProfile target, RankService rankService) {
        super(player, CC.translate("&eGranting &f" + target.getColoredName()), 27);

        this.rankService = rankService;
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
        final List<Rank> ranks = this.rankService.stream()
                .sorted(Comparator.comparingInt(
                                Rank::getWeight)
                        .reversed())
                .collect(Collectors.toList());

        for (int i = 0; i < ranks.size(); i++) {
            final Rank rank = ranks.get(i);
            final ChatColor color = ColorUtility.getColorByRank(rank);

            final Consumer<InventoryClickEvent> action = event -> {
                event.setCancelled(true);

                this.getPlayer().closeInventory();
                new GrantDurationMenu(this.getPlayer(), this.target, rank).updateMenu();
            };

            this.buttons[i] = new Button(Material.WOOL)
                    .setDisplayName(rank.getDisplayName())
                    .setLore(this.getLore(rank))
                    .setData(ColorUtility.findDyeColor(color).getWoolData())
                    .setClickAction(action);
        }
    }

    private String[] getLore(Rank rank) {
        return new String[] {
                CC.translate("&9&m-------------------"),
                CC.translate("&eClick to grant the " + rank.getDisplayName() + " &erank."),
                CC.translate("&9&m-------------------"),
        };
    }
}