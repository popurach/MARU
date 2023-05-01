package com.bird.maru.common.util;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

public class TimeUtil {

    public static LocalDateTime getPreviousAuctionStartDate() {
        return LocalDateTime.now()
                            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                            .minusWeeks(1)
                            .withHour(1)
                            .withMinute(0)
                            .withSecond(0)
                            .withNano(0);
    }

    public static LocalDateTime getPreviousAuctionEndDate(LocalDateTime startDate) {
        return startDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY))
                                           .withHour(23)
                                           .withMinute(59)
                                           .withSecond(59)
                                           .withNano(999999999);
    }

}
