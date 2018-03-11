package com.bar0n.shceduler.services;

import org.quartz.CronExpression;

import java.text.ParseException;
import java.time.*;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyCronExpression {
    private String expression;
    private CronExpression cronExpression;
    private Integer i = null;

    public MyCronExpression(String expression) throws ParseException {
        String[] split = expression.split(" ");
        if (split.length > 5 && split[5].contains("%")) {
            String[] d = split[5].split("%");
            String s = d[0];
            String each = d[1];
            this.i = Integer.parseInt(each);
            split[5] = s;
            String collect = Arrays.stream(split).collect(Collectors.joining(" "));
            cronExpression = new CronExpression(collect);
        } else {
            this.i = 1;
            this.expression = expression;
            cronExpression = new CronExpression(expression);
        }

    }

    public Date getNextValidTimeAfter(Date date, ZonedDateTime start) {

        Pair<Integer, Date> pair = new Pair<>(0, asDate(start));
       /* Stream<Pair<Integer, Date>> iterate = Stream.iterate(pair,
                p -> new Pair<>(p.getKey() + 1, cronExpression.getNextValidTimeAfter(p.getValue())))
                .filter(x -> x.getKey() % i == 0)
                .filter(x -> x.getValue().after(date))
                .limit(10);
        System.out.println();
        iterate.forEach(x -> System.out.println(x));*/
        return Stream.iterate(pair, p -> new Pair<>(p.getFirst() + 1, cronExpression.getNextValidTimeAfter(p.getSecond())))
                .filter(x -> x.getFirst() % i == 0)
                .filter(x -> x.getSecond().after(date))
               // .skip(1)
                .limit(1)
                .reduce((p1, p2) -> p1)
                .map(Pair::getSecond).get();

    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.of("Europe/Helsinki")).toInstant());
    }

    public static Date asDate(ZonedDateTime zonedDateTime) {
        return Date.from(zonedDateTime.toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.of("Europe/Helsinki")).toLocalDate();
    }
}
