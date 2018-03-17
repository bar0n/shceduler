package com.bar0n.shceduler.services;

import sun.util.calendar.BaseCalendar;

import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {
    static public ZoneId zone = ZoneId.of("Europe/Helsinki");

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(zone).toInstant());
    }

    public static ZonedDateTime asDateZoned(LocalDateTime localDateTime) {
        return localDateTime.atZone(zone);
    }

    public static ZonedDateTime asDateZoned(LocalDate localDate) {
        return localDate.atStartOfDay(zone);
    }


    public static Date asDate(ZonedDateTime zonedDateTime) {
        return Date.from(zonedDateTime.toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(zone).toLocalDate();
    }

    public static LocalDateTime now() {
        return ZonedDateTime.now().withZoneSameInstant(DateUtils.zone).toLocalDateTime();
    }

    public static Date asDateSameTime(LocalDateTime localDateTime) {
        return new GregorianCalendar(
                localDateTime.getYear(),
                localDateTime.getMonth().getValue() - 1,
                localDateTime.getDayOfMonth(),
                localDateTime.getHour(),
                localDateTime.getMinute(),
                localDateTime.getSecond()
        ).getTime();
    }

    public static LocalDateTime asLocalDateTimeSameTime(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        LocalTime localTime = LocalTime.of(hour, minute, second, 0);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        LocalDate of = LocalDate.of(year, month + 1, dayOfMonth);

        return LocalDateTime.of(of, localTime);
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), zone);
    }

    public static ZonedDateTime asZonedDateTime(Date date) {
        Instant instant = Instant.ofEpochMilli(date.getTime());

        return instant.atZone(zone);
    }


    public static Date asDate(Date date) {

        Instant instant = Instant.ofEpochMilli(date.getTime());
        LocalDate localDate = asLocalDate(date);
        return date;
    }
}
