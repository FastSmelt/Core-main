package gg.pots.basics.bukkit.chat.impl.grant;

import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.rank.Rank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class GrantProcedureData {

    private final Rank rank;
    private final CoreProfile target;

    private String reason;
    private long duration;
}
