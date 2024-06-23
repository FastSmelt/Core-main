package gg.pots.basics.core;

import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.profile.ProfileService;
import gg.pots.basics.core.rank.Rank;
import gg.pots.basics.core.rank.RankService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class CoreAPI {

    private final ServiceHandler handler;

    /**
     * Find a {@link Rank} by a {@link String}
     *
     * @param rankName the name of the rank
     * @return the rank
     */
    public Rank findRank(String rankName) {
        return this.handler.find(RankService.class).find(rankName);
    }

    /**
     * Find a {@link Rank} by a player through their {@link UUID}
     *
     * @param playerUuid the uuid of the player
     * @return the rank
     */
    public Rank findRank(UUID playerUuid) {
        return this.handler.find(ProfileService.class).find(playerUuid) == null
                ? null
                : this.handler.find(ProfileService.class).find(playerUuid).getPrimaryGrant().getRank();
    }

    /**
     * Find a {@link CoreProfile} by through a {@link UUID}
     *
     * @param playerUuid the uuid of the player.
     * @return the profile.
     */

    public CoreProfile findProfile(UUID playerUuid) {
        return this.handler.find(ProfileService.class).find(playerUuid);
    }

    /**
     * Find a {@link CoreProfile} through a {@link String}
     *
     * @param playerName the name of the player.
     * @return the profile.
     */

    public CoreProfile findProfile(String playerName) {
        return this.handler.find(ProfileService.class).find(playerName);
    }
}
