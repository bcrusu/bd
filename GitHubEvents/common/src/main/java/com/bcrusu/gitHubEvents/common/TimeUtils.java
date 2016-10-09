package com.bcrusu.gitHubEvents.common;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public class TimeUtils {
    public static long truncatedToSeconds(long milli) {
        return truncatedTo(milli, ChronoUnit.SECONDS);
    }

    public static long truncatedToMinutes(long milli) {
        return truncatedTo(milli, ChronoUnit.MINUTES);
    }

    public static long truncatedToDays(long milli) {
        return truncatedTo(milli, ChronoUnit.DAYS);
    }

    private static long truncatedTo(long milli, TemporalUnit temporalUnit) {
        Instant x = Instant.ofEpochMilli(milli);
        return x.truncatedTo(temporalUnit).toEpochMilli();
    }
}
