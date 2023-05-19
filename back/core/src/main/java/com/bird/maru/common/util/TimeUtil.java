package com.bird.maru.common.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TimeUtil {

    public static LocalDateTime getThisWeekStartDateTime() {
        return LocalDateTime.now()
                            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                            .withHour(0)
                            .withMinute(0)
                            .withSecond(0)
                            .withNano(0);
    }

    public static LocalDateTime getCurrentAuctionStartDateTime() {
        return LocalDateTime.now()
                            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                            .withHour(1)
                            .withMinute(0)
                            .withSecond(0)
                            .withNano(0);
    }

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

    public static LocalDateTime getMidnightDate() {
        return LocalDate.now().plusDays(1).atTime(LocalTime.MIDNIGHT);
    }

}
