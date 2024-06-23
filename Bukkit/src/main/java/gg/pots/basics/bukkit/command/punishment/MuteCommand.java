package gg.pots.basics.bukkit.command.punishment;

import gg.pots.basics.bukkit.util.CC;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.expirable.punishment.Punishment;
import gg.pots.basics.core.expirable.punishment.PunishmentType;
import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.profile.ProfileService;
import gg.pots.basics.core.util.DurationUtil;
import io.github.nosequel.command.annotation.Command;
import io.github.nosequel.command.annotation.Param;
import io.github.nosequel.command.bukkit.executor.BukkitCommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MuteCommand {

    private final ProfileService profileService = ServiceHandler.getInstance().find(ProfileService.class);

    @Command(label = "tempmute", permission = "core.tempmute")
    public void tempMute(BukkitCommandExecutor executor, CoreProfile target, @Param(name = "duration", value = "1y") String durationString, @Param(name = "reason", value = "No reason specified.") String reason) {
        long duration;

        final CoreProfile executorProfile = this.profileService.find(executor.getPlayer().getUniqueId());

        if (target.getRank().getWeight() >= executorProfile.getRank().getWeight()) {
            executor.sendMessage(CC.translate("&cYou cannot punish this player."));
            return;
        }

        if (target.getActivePunishments(PunishmentType.MUTE).stream().anyMatch(Punishment::isActive)) {
            executor.sendMessage(CC.translate("&cThis player is already muted."));
            return;
        }

        try {
            duration = DurationUtil.parseTime(durationString);
        } catch (Exception exception) {
            executor.sendMessage(CC.translate("&cInvalid duration format. Please use a valid time format (e.g., 1h, 30m)."));
            return;
        }

        if (duration <= 0L) {
            executor.sendMessage(CC.translate("&cDuration must be greater than 0."));
            return;
        }

        if (duration > 31536000L) {
            duration = 31536000L;
        }

        target.getPunishments().add(new Punishment(UUID.randomUUID(), PunishmentType.MUTE, reason, executor.getPlayer().getName(), duration));
        this.profileService.syncProfile(target);

        target.getPlayer().sendMessage(CC.translate("&cYou have been temporarily muted for &e" + durationString + " &cfor &e" + reason));

        Bukkit.broadcastMessage(CC.translate(target.getColoredName() + " &ahas been temporarily muted by " + executorProfile.getColoredName() + " &afor &e" + durationString));
        executor.sendMessage(CC.translate("&eYou have temporarily muted " + target.getColoredName() + " &efor &e" + durationString));
    }

    @Command(label = "mute", permission = "core.mute")
    public void mute(BukkitCommandExecutor executor, CoreProfile target, @Param(name = "duration", value = "permanent") String durationString, @Param(name = "reason", value = "No reason specified.") String reason) {
        long duration;

        final CoreProfile executorProfile = this.profileService.find(executor.getPlayer().getUniqueId());

        if (target.getRank().getWeight() >= executorProfile.getRank().getWeight()) {
            executor.sendMessage(CC.translate("&cYou cannot punish this player."));
            return;
        }

        if (target.getActivePunishments(PunishmentType.MUTE).stream().anyMatch(Punishment::isActive)) {
            executor.sendMessage(CC.translate("&cThis player is already muted."));
            return;
        }

        if (durationString.equalsIgnoreCase("permanent") || durationString.equalsIgnoreCase("perm")) {
            duration = -1L;
        } else {
            try {
                duration = DurationUtil.parseTime(durationString);
            } catch (Exception exception) {
                duration = -1L;
            }
        }

        target.getPunishments().add(new Punishment(UUID.randomUUID(), PunishmentType.MUTE, reason, executor.getPlayer().getName(), duration));
        this.profileService.syncProfile(target);

        target.getPlayer().sendMessage(CC.translate("&eYou have been muted for &e" + reason));

        Bukkit.broadcastMessage(CC.translate(target.getColoredName() + " &ahas been muted by " + executorProfile.getColoredName() + " &afor &e" + durationString));
        executor.sendMessage(CC.translate("&eYou have muted " + target.getColoredName()));
    }

    @Command(label = "unmute", permission = "core.unmute")
    public void unban(BukkitCommandExecutor executor, CoreProfile target) {
        if (target.getActivePunishments(PunishmentType.MUTE).stream().noneMatch(Punishment::isActive)) {
            executor.sendMessage(CC.translate("&cThis player is not banned."));
            return;
        }

        target.getActivePunishments(PunishmentType.MUTE).stream()
                .filter(Punishment::isActive)
                .forEach(punishment -> punishment.setActive(false));

        this.profileService.syncProfile(target);

        final CoreProfile executorProfile = this.profileService.find(executor.getPlayer().getUniqueId());

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("core.staff")) {
                player.sendMessage(CC.translate(target.getColoredName() + " &ehas been unmuted by " + executorProfile.getColoredName()));
            }
        }

        executor.sendMessage(CC.translate("&eYou have unmuted " + target.getColoredName()));
    }
}
