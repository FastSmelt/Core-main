package gg.pots.basics.bukkit.command.grant.menu.duration;

import gg.pots.basics.core.util.DurationUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GrantDurationType {

    DAY("1 day", DurationUtil.parseTime("1d")),
    WEEK("1 week", DurationUtil.parseTime("1w")),
    MONTH("1 month", DurationUtil.parseTime("1M")),
    YEAR("1 year", DurationUtil.parseTime("1y")),
    FOREVER("Forever", -1L);

    private final String display;
    private final long duration;
}