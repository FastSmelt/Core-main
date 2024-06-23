package gg.pots.basics.bukkit.command.color.menu;

import gg.pots.basics.bukkit.CoreSpigotPlugin;
import gg.pots.basics.bukkit.util.CC;
import gg.pots.basics.bukkit.util.color.ColorUtility;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.profile.ProfileService;
import io.github.nosequel.menu.Menu;
import io.github.nosequel.menu.buttons.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;

public class ColorMenu extends Menu {

    private final ProfileService profileService = ServiceHandler.getInstance().find(ProfileService.class);

    public ColorMenu(Player player) {
        super(player, CC.translate("&fColor Menu"), 27);
    }

    @Override
    public void tick() {
        final CoreProfile coreProfile = this.profileService.find(this.getPlayer().getUniqueId());

        if (coreProfile == null) {
            this.getPlayer().closeInventory();
            return;
        }

        Arrays.stream(ChatColor.values())
                .filter(ChatColor::isColor)
                .forEach(color -> {
                    if (!this.getPlayer().hasPermission("color." + color.name().toLowerCase())) {
                        this.buttons[color.ordinal()] = new Button(Material.STAINED_GLASS_PANE)
                                .setDisplayName(color + color.name())
                                .setData(ColorUtility.findDyeColor(color).getWoolData())
                                .setLore(new String[] {
                                        CC.translate("&7Chat colors change the color"),
                                        CC.translate("&7of your messages in chat."),
                                        "",
                                        CC.translate("&7Preview: &f" + this.getPlayer().getName() + color + " Hello World!"),
                                        "",
                                        CC.translate("&cYou do not have permission to use this color.")
                                });
                    } else {
                        this.buttons[color.ordinal()] = new Button(Material.WOOL)
                                .setDisplayName(color + color.name())
                                .setData(ColorUtility.findDyeColor(color).getWoolData())
                                .setLore(new String[] {
                                        CC.translate("&7Chat colors change the color"),
                                        CC.translate("&7of your messages in chat."),
                                        "",
                                        CC.translate("&7Preview: &f" + this.getPlayer().getName() + color + " Hello World!"),
                                        "",
                                        CC.translate("&aClick to select this color.")
                                }).setClickAction(event -> {
                                    if (!this.getPlayer().hasPermission("color." + color.name().toLowerCase())) {
                                        this.getPlayer().sendMessage(CC.translate("&cYou do not have permission to use this color."));
                                        return;
                                    }

                                    if (this.getPlayer().hasMetadata("color")) {
                                        this.getPlayer().removeMetadata("color", CoreSpigotPlugin.instance);
                                        coreProfile.setCustomColor(null);
                                        this.profileService.syncProfile(coreProfile);
                                    }

                                    this.getPlayer().setMetadata("color", new FixedMetadataValue(CoreSpigotPlugin.instance, color.name()));
                                    coreProfile.setCustomColor(color);
                                    this.profileService.syncProfile(coreProfile);

                                    this.getPlayer().sendMessage(CC.translate("&aYou have selected the color " + color + color.name() + "&a."));
                                    this.getPlayer().closeInventory();
                                });
                    }
                });
    }
}