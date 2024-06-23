package gg.pots.basics.bukkit.command.setting.menu;

import gg.pots.basics.bukkit.CoreSpigotPlugin;
import gg.pots.basics.bukkit.util.CC;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.profile.ProfileService;
import io.github.nosequel.menu.Menu;
import io.github.nosequel.menu.buttons.Button;
import io.github.nosequel.menu.filling.FillingType;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SettingsMenu extends Menu {

    private final ProfileService profileService = ServiceHandler.getInstance().find(ProfileService.class);

    private final int[] EMPTY_SLOTS = new int[] {
            0,1,2,3,4,5,6,7,8,9,17,18,19,20,21,22,23,24,25,26
    };

    public SettingsMenu(Player player) {
        super(player, "Settings", 27);

        this.addFiller(FillingType.BORDER);
        this.setFillerType(new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLACK.getData()));
    }

    @Override
    public void tick() {
        final CoreProfile coreProfile = CoreSpigotPlugin.instance.handler.find(ProfileService.class)
                .find(this.getPlayer().getUniqueId());

        for (int i : EMPTY_SLOTS) {
            this.buttons[i] = new Button(this.getFillerType())
                    .setDisplayName(" ");

            this.buttons[10] = new Button(Material.BOOK_AND_QUILL)
                    .setDisplayName(CC.translate("&e&lServer Broadcasts"))
                    .setLore(new String[] {
                            "",
                            CC.translate("&7Would you like to"),
                            CC.translate("&7be able to view server"),
                            CC.translate("&7broadcasts?"),
                            "",
                            (coreProfile.isCanViewBroadcasts() ? ChatColor.GREEN + ChatColor.BOLD.toString() + "✔ " : ChatColor.DARK_GRAY + ChatColor.BOLD.toString() + "✔ ") + "&fEnabled",
                            (!coreProfile.isCanViewBroadcasts() ? ChatColor.RED + ChatColor.BOLD.toString() + "✖ " : ChatColor.DARK_GRAY + ChatColor.BOLD.toString() + "✖ ") + "&fDisabled",
                            ""
                    })
                    .setClickAction(event -> {
                        event.setCancelled(true);

                        coreProfile.setCanViewBroadcasts(!coreProfile.isCanViewBroadcasts());
                        this.profileService.syncProfile(coreProfile);

                        this.updateMenu();
                    });

            this.buttons[11] = new Button(Material.PAPER)
                    .setDisplayName(CC.translate("&e&lToggle Private Messages"))
                    .setLore(new String[] {
                            "",
                            CC.translate("&7Would you like to"),
                            CC.translate("&7be able to receive"),
                            CC.translate("&7private messages?"),
                            "",
                            (coreProfile.isTogglePrivateMessages() ? ChatColor.GREEN + ChatColor.BOLD.toString() + "✔ " : ChatColor.DARK_GRAY + ChatColor.BOLD.toString() + "✔ ") + "&fEnabled",
                            (!coreProfile.isTogglePrivateMessages() ? ChatColor.RED + ChatColor.BOLD.toString() + "✖ " : ChatColor.DARK_GRAY + ChatColor.BOLD.toString() + "✖ ") + "&fDisabled",
                            ""
                    })
                    .setClickAction(event -> {
                        event.setCancelled(true);

                        coreProfile.setTogglePrivateMessages(!coreProfile.isTogglePrivateMessages());
                        this.profileService.syncProfile(coreProfile);

                        this.updateMenu();
                    });
        }
    }
}