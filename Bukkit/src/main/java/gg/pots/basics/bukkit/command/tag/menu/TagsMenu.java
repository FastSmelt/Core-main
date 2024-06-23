package gg.pots.basics.bukkit.command.tag.menu;

import gg.pots.basics.bukkit.util.CC;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.profile.ProfileService;
import gg.pots.basics.core.tag.Tag;
import gg.pots.basics.core.tag.TagService;
import io.github.nosequel.menu.buttons.Button;
import io.github.nosequel.menu.pagination.PaginatedMenu;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class TagsMenu extends PaginatedMenu {

    private final ProfileService profileService = ServiceHandler.getInstance().find(ProfileService.class);
    private final TagService tagService = ServiceHandler.getInstance().find(TagService.class);

    public TagsMenu(Player player) {
        super(player, "Tags", 27);

        this.setPreviousPageButton(new Button(Material.CARPET)
                .setDisplayName(CC.translate("&cPrevious Page"))
                .setData(DyeColor.RED.getWoolData()));

        this.setNextPageButton(new Button(Material.CARPET)
                .setDisplayName(CC.translate("&aNext Page"))
                .setData(DyeColor.GREEN.getWoolData()));
    }

    @Override
    public void tick() {
        for (int i = 0; i < this.tagService.getTags().size(); i++) {
            final Tag tag = this.tagService.getTags().get(i);

            boolean hasPermission = this.getPlayer().hasPermission(tag.getPermission());

            this.buttons[i] = new Button(tag.getIcon())
                    .setDisplayName(tag.getDisplayName())
                    .setLore(this.getLore())
                    .setClickAction(event -> {
                        if (hasPermission) {
                            final CoreProfile coreProfile = this.profileService.find(this.getPlayer().getUniqueId());

                            if (coreProfile == null) {
                                coreProfile.getPlayer().sendMessage(CC.translate("&cYou do not have a profile."));
                                return;
                            }

                            coreProfile.setActiveTag(tag);
                            coreProfile.getPlayer().sendMessage(new String[] {
                                    CC.translate("&eYou have set your active tag to " + tag.getDisplayName()),
                                    CC.translate("&cIf you wish to remove your active tag, please use /cleartag.")
                            });
                            this.updateMenu();
                        } else {
                            this.getPlayer().sendMessage(CC.translate("&cYou do not have permission to use this tag."));
                        }
                        event.setCancelled(true);
                    });
        }
    }

    private String[] getLore() {
        final CoreProfile coreProfile = this.profileService.find(this.getPlayer().getUniqueId());

        return new String[] {
                CC.translate(""),
                CC.translate(coreProfile.getActiveTag() == null ? "&cYou do not have an active tag." : "&aYour active tag is: &f" + coreProfile.getActiveTag().getDisplayName())
        };
    }
}
