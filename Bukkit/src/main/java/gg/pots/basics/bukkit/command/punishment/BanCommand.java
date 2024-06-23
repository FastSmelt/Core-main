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

public class BanCommand {

    private final ProfileService profileService = ServiceHandler.getInstance().find(ProfileService.class);

    @Command(label = "tempban", permission = "core.tempban")
    public void tempBan(BukkitCommandExecutor executor, CoreProfile target, @Param(name = "duration", value = "1y") String durationString, @Param(name = "reason", value = "No reason specified.") String reason) {
        long duration;

        final CoreProfile executorProfile = this.profileService.find(executor.getPlayer().getUniqueId());

        if (target.getRank().getWeight() >= executorProfile.getRank().getWeight()) {
            executor.sendMessage(CC.translate("&cYou cannot punish this player."));
            return;
        }

        if (target.getActivePunishments(PunishmentType.BAN).stream().anyMatch(Punishment::isActive)) {
            executor.sendMessage(CC.translate("&cThis player is already banned."));
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

        target.getPunishments().add(new Punishment(UUID.randomUUID(), PunishmentType.BAN, reason, executor.getPlayer().getName(), duration));
        this.profileService.syncProfile(target);

        Player player = Bukkit.getPlayer(target.getPlayer().getUniqueId());
        if (player != null) {
            player.kickPlayer(CC.translate("&cYou have been temporarily banned for &e" + durationString + " &cfor &e" + reason));
        }

        Bukkit.broadcastMessage(CC.translate(target.getColoredName() + " &ahas been temporarily banned by " + executorProfile.getColoredName() + " &afor &e" + durationString));
        executor.sendMessage(CC.translate("&eYou have temporarily banned " + target.getColoredName() + " &efor &e" + durationString));
    }

    @Command(label = "ban", permission = "core.ban")
    public void ban(BukkitCommandExecutor executor, CoreProfile target, @Param(name = "duration", value = "permanent") String durationString, @Param(name = "reason", value = "No reason specified.") String reason) {
        long duration;

        final CoreProfile executorProfile = this.profileService.find(executor.getPlayer().getUniqueId());

        if (target.getRank().getWeight() >= executorProfile.getRank().getWeight()) {
            executor.sendMessage(CC.translate("&cYou cannot punish this player."));
            return;
        }

        if (target.getActivePunishments(PunishmentType.BAN).stream().anyMatch(Punishment::isActive)) {
            executor.sendMessage(CC.translate("&cThis player is already banned."));
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

        target.getPunishments().add(new Punishment(UUID.randomUUID(), PunishmentType.BAN, reason, executor.getPlayer().getName(), duration));
        this.profileService.syncProfile(target);

        Player player = Bukkit.getPlayer(target.getPlayer().getUniqueId());
        if (player != null) {
            player.kickPlayer(CC.translate("&cYou have been banned for &e" + durationString + " &cfor &e" + reason));
        }

        Bukkit.broadcastMessage(CC.translate(target.getColoredName() + " &ahas been banned by " + executorProfile.getColoredName() + " &afor &e" + durationString));
        executor.sendMessage(CC.translate("&eYou have banned " + target.getColoredName()));
    }

    @Command(label = "unban", permission = "core.unban")
    public void unban(BukkitCommandExecutor executor, CoreProfile target) {
        if (target.getActivePunishments(PunishmentType.BAN).stream().noneMatch(Punishment::isActive)) {
            executor.sendMessage(CC.translate("&cThis player is not banned."));
            return;
        }

        target.getActivePunishments(PunishmentType.BAN).stream()
                .filter(Punishment::isActive)
                .forEach(punishment -> punishment.setActive(false));

        this.profileService.syncProfile(target);

        final CoreProfile executorProfile = this.profileService.find(executor.getPlayer().getUniqueId());

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("core.staff")) {
                player.sendMessage(CC.translate(target.getColoredName() + " &ehas been unbanned by " + executorProfile.getColoredName()));
            }
        }

        executor.sendMessage(CC.translate("&eYou have unbanned " + target.getColoredName()));
    }
}
