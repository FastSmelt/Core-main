package gg.pots.basics.bukkit.command.grant.menu.reason;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GrantReasonType {

    PURCHASED("Purchased"),
    GIVEAWAY("Giveaway Winner"),
    PROMOTION("Promoted"),
    EVENT("Won an Event"),
    ISSUES("An issue occurred"),
    UNSPECIFIED("Unspecified");

    private final String display;
}