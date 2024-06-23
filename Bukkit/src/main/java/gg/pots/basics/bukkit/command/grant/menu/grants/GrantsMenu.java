package gg.pots.basics.bukkit.command.grant.menu.grants;

import gg.pots.basics.bukkit.util.CC;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.expirable.grant.Grant;
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
public class GrantsMenu extends PaginatedMenu {

    private final ProfileService profileService = ServiceHandler.getInstance().find(ProfileService.class);

    private final CoreProfile target;

    public GrantsMenu(Player player, CoreProfile target) {
        super(player, CC.translate("&eGrants of &a" + target.getColoredName()), 27);

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
        final List<Grant> grants = new ArrayList<>(this.profileService
                .find(target.getUuid())
                .getGrants());

        final Player player = this.getPlayer();
        final CoreProfile profile = this.profileService.find(player.getUniqueId());

        for (int i = 0; i < grants.size(); i++) {
            final Grant grant = grants.get(i);

            this.buttons[i] = new Button(Material.WOOL)
                    .setDisplayName((grant.isActive() ? CC.translate("&a(Active)") : CC.translate("&c(Inactive)")) + " &8#" + grant.getUuid().toString().substring(0, 8))
                    .setLore(this.getLore(grant))
                    .setData(grant.isActive() ? DyeColor.LIME.getWoolData() : DyeColor.RED.getWoolData())
                    .setClickAction(action -> {
                        if (!profile.canGrant(grant.getRank())) {
                            this.getPlayer().sendMessage(CC.translate("&cYou do not have permission to modify this grant."));
                            return;
                        }

                        if (grant.getRank().isDefaultRank()) {
                            this.getPlayer().sendMessage(CC.translate("&cYou cannot modify the default rank."));
                            return;
                        }

                        grant.setActive(!grant.isActive());
                        this.profileService.syncProfile(target);
                        action.setCancelled(true);
                        this.updateMenu();
                    });

        }
    }

    private String[] getLore(Grant grant) {
        return new String[] {
                CC.translate("&a+" + new Date(grant.getStartEpoch())),
                "",
                CC.translate("&7Target: &a" + target.getColoredName()),
                CC.translate("&7Rank: " + grant.getRank().getDisplayName()),
                CC.translate("&7Duration: &f" + (grant.isActive() ? grant.getExpirationEpoch() == -1L ? "Permanent" : DurationUtil.millisToRoundedTime(grant.getExpirationEpoch() - System.currentTimeMillis()) : DurationUtil.unixToDate(grant.getExpirationEpoch()))),
                "",
                CC.translate("&7Issued By: &f" + grant.getExecutor()),
                CC.translate("&7Issued Reason: &f" + grant.getReason()),
                "",
                CC.translate("&eClick here to modify this grant.")
        };
    }
}