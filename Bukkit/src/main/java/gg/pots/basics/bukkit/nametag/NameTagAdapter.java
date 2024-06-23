package gg.pots.basics.bukkit.nametag;

import gg.pots.basics.bukkit.util.CC;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.profile.ProfileService;
import io.github.thatkawaiisam.ostentus.BufferedNametag;
import io.github.thatkawaiisam.ostentus.OstentusAdapter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class NameTagAdapter implements OstentusAdapter {

    private final ProfileService profileService = ServiceHandler.getInstance().find(ProfileService.class);

    @Override
    public List<BufferedNametag> getPlate(Player player) {
        final List<BufferedNametag> bufferedNametagList = new ArrayList<>();
        final CoreProfile coreProfile = this.profileService.find(player.getUniqueId());
        final String prefix = coreProfile.getPrimaryGrant().getRank().getColor().toString();

        final BufferedNametag bufferedNametag = new BufferedNametag("tagGroup",
                CC.translate(prefix),
                CC.translate(""), true, player);

        bufferedNametagList.add(bufferedNametag);

        return bufferedNametagList;
    }

    @Override
    public boolean showHealthBelowName(Player player) {
        return false;
    }
}
