package gg.pots.basics.bukkit.command.rank.menu;

import gg.pots.basics.bukkit.util.CC;
import gg.pots.basics.bukkit.util.color.ColorUtility;
import gg.pots.basics.core.ServiceHandler;
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
public class RanksMenu extends PaginatedMenu {

    private final RankService rankService = ServiceHandler.getInstance().find(RankService.class);

    public RanksMenu(Player player) {
        super(player, "Ranks", 27);

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
                .sorted(Comparator.comparingInt(Rank::getWeight).reversed()).collect(Collectors.toList());

        for (int i = 0; i < ranks.size(); i++) {
            final Rank rank = ranks.get(i);
            final ChatColor chatColor = ColorUtility.getColorByRank(rank);

            final Consumer<InventoryClickEvent> action = event -> {
                event.setCancelled(true);
            };

            this.buttons[i] = new Button(Material.INK_SACK)
                    .setDisplayName(rank.getDisplayName())
                    .setLore(this.getLore(rank))
                    .setData(ColorUtility.findDyeColor(chatColor).getDyeData())
                    .setClickAction(action);
        }
    }

    private String[] getLore(Rank rank) {
        return new String[] {
                CC.translate("&e&m-------------------------"),
                CC.translate("&6Metadata:"),
                CC.translate("&fWeight: &6" + rank.getWeight()),
                CC.translate("&fInherits: &6" + rank.getInherits().size()),
                CC.translate("&fPermissions: &6" + rank.getPermissions().size()),
                "",
                CC.translate("&6Display Information:"),
                CC.translate("&fPrefix: &6" + rank.getPrefix()),
                CC.translate("&fColor: &r" + rank.getColor().name()),
                CC.translate("&fDisplay Name: &6" + rank.getDisplayName()),
                CC.translate("&e&m-------------------------")
        };
    }
}