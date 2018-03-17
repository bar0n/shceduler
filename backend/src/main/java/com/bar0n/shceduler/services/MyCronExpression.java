package com.bar0n.shceduler.services;

import org.quartz.CronExpression;

import java.text.ParseException;
import java.time.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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

    public LocalDateTime getNextValidTimeAfter(LocalDateTime date, LocalDateTime start) {
        Pair<Integer, Date> pair = new Pair<>(0, DateUtils.asDate(start));
        Date date1 = Stream.iterate(pair, p -> new Pair<>(p.getFirst() + 1, cronExpression.getNextValidTimeAfter(p.getSecond())))
                .filter(x -> x.getFirst() % i == 0)
                .filter(x -> x.getSecond().after(DateUtils.asDate(date)))
                // .skip(1)
                .limit(1)
                .reduce((p1, p2) -> p1)
                .map(Pair::getSecond).get();
        return DateUtils.asLocalDateTimeSameTime(date1);
    }

    public List<LocalDateTime> getDatesBetween(LocalDateTime fromLocal, LocalDateTime stopLocal,
                                               LocalDateTime startLocal) {
        Date start = DateUtils.asDateSameTime(startLocal);
        Date stop = DateUtils.asDateSameTime(stopLocal);
        Date from = DateUtils.asDateSameTime(fromLocal);
        List<LocalDateTime> result = new ArrayList<>();
        int iter = 0;
        while (start.before(stop)) {
            iter++;
            Date nextValidTimeAfter = cronExpression.getNextValidTimeAfter(start);
            if (start.after(from) && start.before(stop) && iter % i == 0) {
                result.add(DateUtils.asLocalDateTimeSameTime(start));
            }

            start = nextValidTimeAfter;
        }

        return result;

    }


}
