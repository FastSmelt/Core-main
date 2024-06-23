package gg.pots.basics.bukkit.command.permission;

import gg.pots.basics.bukkit.util.CC;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.profile.ProfileService;
import io.github.nosequel.command.annotation.Command;
import io.github.nosequel.command.bukkit.executor.BukkitCommandExecutor;

public class PermissionCommands {

    private final ProfileService profileService = ServiceHandler.getInstance().find(ProfileService.class);

    @Command(label = "permissions", permission = "core.permissions")
    public void permission(BukkitCommandExecutor executor, CoreProfile target, String type, String permission) {
        switch (type.toLowerCase()) {
            case "add":
                if (target.getPermissions().contains(permission)) {
                    executor.sendMessage(CC.translate("&cThat player already has that permission."));
                    return;
                }

                target.getPermissions().add(permission);
                this.profileService.syncProfile(target);

                executor.sendMessage(CC.translate("&aAdded permission &f" + permission + " &ato &f" + target.getColoredName() + "&a."));
                target.getPlayer().sendMessage(new String[] {
                        CC.translate("&eYour &apermissions &ehave changed."),
                        CC.translate("&ePlease &crelog &eto apply the changes.")
                });
                break;
            case "remove":
                if (!target.getPermissions().contains(permission)) {
                    executor.sendMessage(CC.translate("&cThat player does not have that permission."));
                    return;
                }

                target.getPermissions().remove(permission);
                this.profileService.syncProfile(target);

                executor.sendMessage(CC.translate("&aRemoved permission &f" + permission + " &afrom &f" + target.getColoredName() + "&a."));
                target.getPlayer().sendMessage(new String[] {
                        CC.translate("&eYour &apermissions &ehave changed."),
                        CC.translate("&ePlease &crelog &eto apply the changes.")
                });
        }
    }

    @Command(label = "viewpermissions", permission = "core.viewpermissions")
    public void viewPermissions(BukkitCommandExecutor executor, CoreProfile target) {
        executor.sendMessage(CC.translate("&aPermissions for &f" + target.getColoredName() + "&a:"));
        target.getPermissions().forEach(permission -> executor.sendMessage(CC.translate("&f- &a" + permission)));
    }

    @Command(label = "wipeprofile" ,permission = "core.wipeprofile")
    public void wipeProfile(BukkitCommandExecutor executor, CoreProfile target) {
        final CoreProfile profile = this.profileService.find(executor.getPlayer().getUniqueId());

        if (profile.equals(target.getUuid())) {
            executor.sendMessage(CC.translate("&cYou cannot wipe your own profile."));
            return;
        }

        if (!executor.getPlayer().getName().equalsIgnoreCase("Staud")) {
            executor.sendMessage(CC.translate("&cYou do not have permission to wipe this profile."));
            return;
        }

        this.profileService.deleteProfile(target);
        this.profileService.syncProfile(target);
        target.getPlayer().kickPlayer(CC.translate("&cYour profile has been wiped."));
        executor.sendMessage(CC.translate("&aSuccessfully wiped profile &f" + target.getColoredName() + "&a."));
    }
}