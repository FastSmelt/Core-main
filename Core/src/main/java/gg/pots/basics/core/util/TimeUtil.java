package gg.pots.basics.core.util;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class TimeUtil {

    private static final long TICK_MS = 50;
    private static final long SECOND_MS = 1000;
    private static final long MINUTE_MS = SECOND_MS * 60;
    private static final long HOUR_MS = MINUTE_MS * 60;
    private static final long DAY_MS = HOUR_MS * 24;
    private static final long MONTH_MS = DAY_MS * 30;
    private static final long YEAR_MS = DAY_MS * 365;

    private static final Map<String, Long> unitMultipliers = new HashMap<>();

    static {
        addTimeMultiplier(1, "ms", "milli", "millis", "millisecond", "milliseconds");
        addTimeMultiplier(TICK_MS, "t", "tick", "ticks");
        addTimeMultiplier(SECOND_MS, "s", "sec", "secs", "second", "seconds");
        addTimeMultiplier(MINUTE_MS, "m", "min", "mins", "minute", "minutes");
        addTimeMultiplier(HOUR_MS, "h", "hour", "hours", "hr");
        addTimeMultiplier(DAY_MS, "d", "day", "days");
        addTimeMultiplier(MONTH_MS, "mo", "month", "months");
        addTimeMultiplier(YEAR_MS, "y", "year", "years");
    }

    private final long milliseconds;

    public TimeUtil(long milliseconds) {
        if (milliseconds < 0) {
            throw new IllegalArgumentException("Number of milliseconds cannot be less than 0");
        }

        this.milliseconds = milliseconds;
    }

    private static void addTimeMultiplier(long multiplier, String... keys) {
        for (String key : keys) {
            unitMultipliers.put(key, multiplier);
        }
    }

    private static long appendTime(long time, long unitInMS, String name, StringBuilder builder) {
        long timeInUnits = (time - (time % unitInMS)) / unitInMS;

        if (timeInUnits > 0) {
            builder.append(", ").append(timeInUnits).append(' ').append(name);
        }

        return time - timeInUnits * unitInMS;
    }

    @Override
    public String toString() {
        final StringBuilder timeString = new StringBuilder();

        long time = this.milliseconds;

        time = appendTime(time, YEAR_MS, "years", timeString);
        time = appendTime(time, MONTH_MS, "months", timeString);
        time = appendTime(time, DAY_MS, "days", timeString);
        time = appendTime(time, HOUR_MS, "hours", timeString);
        time = appendTime(time, MINUTE_MS, "minutes", timeString);
        time = appendTime(time, SECOND_MS, "seconds", timeString);

        if (time != 0) {
            timeString.append("| ").append(time).append(" ms");
        }

        if (timeString.length() == 0) {
            return "0 seconds";
        }
        return timeString.substring(2, timeString.indexOf("|"));
    }
}