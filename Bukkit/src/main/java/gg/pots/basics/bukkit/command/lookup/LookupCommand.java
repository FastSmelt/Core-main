package gg.pots.basics.bukkit.command.lookup;

import gg.pots.basics.bukkit.util.CC;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.profile.ProfileService;
import gg.pots.basics.core.util.TimeUtil;
import io.github.nosequel.command.annotation.Command;
import io.github.nosequel.command.bukkit.executor.BukkitCommandExecutor;

import java.text.SimpleDateFormat;
import java.util.Arrays;

public class LookupCommand {

    private final ProfileService profileService = ServiceHandler.getInstance().find(ProfileService.class);

    @Command(label = "lookup", permission = "core.staff")
    public void lookup(BukkitCommandExecutor executor, CoreProfile target) {
        final CoreProfile profile = this.profileService.find(target.getUuid());

        try {
            Arrays.asList(
                    CC.translate("&7&m------------ &r &dProfile (&7" + profile.getName() + "&d) &7&m------------"),
                    "",
                    CC.translate("&5Info"),
                    CC.translate(" &dUUID: &f" + profile.getUuid().toString().substring(0, 8)),
                    CC.translate(" &dName: &f" + profile.getColoredName()),
                    CC.translate(" &dRank: &f" + profile.getRank().getDisplayName()),
                    CC.translate(" &dBanned: &f" + (profile.isBanned() ? "&aTrue" : "&cFalse")),
                    "",
                    CC.translate("&5Dates"),
                    CC.translate(" &dFirst Join: &f" + new SimpleDateFormat("EEE, MMM d, yyyy").format(profile.getFirstJoin())),
                    CC.translate("   &7- " + new TimeUtil((System.currentTimeMillis() - profile.getFirstJoin())) + " ago."),
                    CC.translate(" &dLast Join: &f" + new SimpleDateFormat("EEE, MMM d, yyyy").format(profile.getLastJoin())),
                    CC.translate("   &7- " + new TimeUtil((System.currentTimeMillis() - profile.getLastJoin())) + " ago."),
                    CC.translate("&7&m-----------------------------------------------")
            ).forEach(string -> executor.sendMessage(CC.translate(String.valueOf(string))));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}