package com.bar0n.shceduler.services;

import com.bar0n.shceduler.data.ScheduleLogRepository;
import com.bar0n.shceduler.data.ScheduleRepository;
import com.bar0n.shceduler.model.Schedule;
import com.bar0n.shceduler.model.ScheduleLog;
import org.quartz.CronExpression;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

/**
 * Created by dbaron
 */
@Component
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleLogRepository scheduleLogRepository;
    private final MailService mailService;

    public ScheduleService(ScheduleRepository scheduleRepository, ScheduleLogRepository scheduleLogRepository, MailService mailService) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleLogRepository = scheduleLogRepository;
        this.mailService = mailService;
    }

    public ZonedDateTime getNextTime(ZonedDateTime time, String cronExpressionTxt) {
        CronExpression cronExpression = null;
        try {
            cronExpression = new CronExpression(cronExpressionTxt);
        } catch (ParseException e) {
            throw new RuntimeException("parse error", e);
        }
        Date from = Date.from(time.toInstant());
        Date nextValidTimeAfter = cronExpression.getNextValidTimeAfter(from);
        ZoneId zone = time.getZone();
        return ZonedDateTime.ofInstant(nextValidTimeAfter.toInstant(), zone);
    }

    public void fireJob() {
        List<Schedule> allByNextLessThan = scheduleRepository.findAllByNextLessThan(ZonedDateTime.now());
        allByNextLessThan.forEach(this::handle);
    }

    private void handle(Schedule schedule) {

        ZonedDateTime nextTime = getNextTime(schedule);
        schedule.setNext(nextTime);
        scheduleRepository.save(schedule);
        ScheduleLog scheduleLog = new ScheduleLog(ZonedDateTime.now(), schedule);
        scheduleLogRepository.save(scheduleLog);
        mailService.sendNotificationEmail(schedule);

    }

    private ZonedDateTime getNextTime(Schedule schedule) {
        return getNextTime(schedule.getNext(), schedule.getCron());
    }
}
