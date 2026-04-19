package com.cozisoft.starter.shared.core;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@UtilityClass
public class DateUtils {
    public static final String UTC_ZONE_ID = "UTC";

    public ZonedDateTime fromInstant(java.time.Instant instant) {
        return ZonedDateTime.ofInstant(instant, ZoneId.of(UTC_ZONE_ID));
    }

    public ZonedDateTime now() {
        return ZonedDateTime.now(ZoneId.of(UTC_ZONE_ID));
    }

    public long getSeconds(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toLocalDateTime().atZone(ZoneId.of(UTC_ZONE_ID)).toInstant().getEpochSecond();
    }

    public ZonedDateTime fromSeconds(long seconds) {
        return ZonedDateTime.ofInstant(java.time.Instant.ofEpochSecond(seconds), ZoneId.of(UTC_ZONE_ID));
    }

    public LocalDate toLocalDate(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toLocalDate();
    }

    public long computeDaysDifference(LocalDate from, LocalDate to) {
        return ChronoUnit.DAYS.between(from, to);
    }
}
