package gg.pots.basics.bukkit.command.grant;

import gg.pots.basics.bukkit.command.grant.menu.GrantMenu;
import gg.pots.basics.bukkit.util.CC;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.expirable.grant.Grant;
import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.profile.ProfileService;
import gg.pots.basics.core.rank.Rank;
import gg.pots.basics.core.rank.RankService;
import gg.pots.basics.core.util.DurationUtil;
import io.github.nosequel.command.annotation.Command;
import io.github.nosequel.command.annotation.Param;
import io.github.nosequel.command.bukkit.executor.BukkitCommandExecutor;

import java.util.UUID;

public class GrantCommand {

    private final ProfileService profileService = ServiceHandler.getInstance().find(ProfileService.class);
    private final RankService rankService = ServiceHandler.getInstance().find(RankService.class);

    @Command(label = "grant", permission = "core.grant", userOnly = true)
    public void grant(BukkitCommandExecutor executor, CoreProfile target) {
        new GrantMenu(executor.getPlayer(), target, this.rankService).updateMenu();
    }

    @Command(label = "manualgrant", permission = "core.grant")
    public void manualGrant(BukkitCommandExecutor executor, CoreProfile target, Rank rank, @Param(name = "duration", value = "permanent") String durationString, @Param(name = "reason", value = "No reason specified.") String reason) {
        long duration;

        if (durationString.equalsIgnoreCase("permanent") || durationString.equalsIgnoreCase("perm")) {
            duration = -1L;
        } else {
            try {
                duration = DurationUtil.parseTime(durationString);
            } catch (Exception exception) {
                duration = -1L;
            }
        }

        final CoreProfile profile = this.profileService.find(executor.getPlayer().getUniqueId());

        if (!profile.canGrant(rank)) {
            executor.sendMessage(CC.translate("&cYou do not have permission to grant this rank."));
            return;
        }

        target.getGrants().add(new Grant(UUID.randomUUID(), rank, executor.getPlayer().getName(), reason, duration));
        this.profileService.syncProfile(target);

        executor.sendMessage(CC.translate("&aYou have granted &f" + target.getName() + " " + rank.getDisplayName() + " &afor &f" + durationString + "&a."));
    }
}