package com.bar0n.shceduler.services;

import com.bar0n.shceduler.data.ScheduleLogRepository;
import com.bar0n.shceduler.data.ScheduleRepository;
import com.bar0n.shceduler.model.Schedule;
import com.bar0n.shceduler.model.ScheduleLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dbaron
 */
@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleLogRepository scheduleLogRepository;
    private final MailService mailService;
    private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);

    public ScheduleService(ScheduleRepository scheduleRepository, ScheduleLogRepository scheduleLogRepository, MailService mailService) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleLogRepository = scheduleLogRepository;
        this.mailService = mailService;
    }

    public List<ZonedDateTime> getDatesBetween(ZonedDateTime start, ZonedDateTime stop, String cronExpressionTxt, ZonedDateTime fromDate) {
        MyCronExpression cronExpression = null;
        fromDate = start.compareTo(fromDate) < 0 ? start : fromDate;
        try {
            cronExpression = new MyCronExpression(cronExpressionTxt);
        } catch (ParseException e) {
            throw new RuntimeException("parse error", e);
        }
        List<ZonedDateTime> result = new ArrayList<>();
        while (start.compareTo(stop) < 0) {
            Date from = Date.from(start.toInstant());
            Date nextValidTimeAfter = cronExpression.getNextValidTimeAfter(from, fromDate);
            ZoneId zone = start.getZone();
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(nextValidTimeAfter.toInstant(), zone);
            start = zonedDateTime;
            result.add(zonedDateTime);
        }
        return result;
    }

    public ZonedDateTime getNextTime(ZonedDateTime time, String cronExpressionTxt, ZonedDateTime start) {
        MyCronExpression cronExpression = null;
        try {
            cronExpression = new MyCronExpression(cronExpressionTxt);
        } catch (ParseException e) {
            throw new RuntimeException("parse error", e);
        }
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime t = (now.compareTo(time) > 0) ? now : time;
        Date from = Date.from(t.toInstant());
        Date nextValidTimeAfter = cronExpression.getNextValidTimeAfter(from, start);
        ZoneId zone = time.getZone();
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(nextValidTimeAfter.toInstant(), zone);
        logger.debug("getNextTime time: {}, now:{}, max(time,now):{}, nextTime:{}", time, now, t, zonedDateTime);
        return zonedDateTime;
    }

    @Transactional
    public void fireJob() {
        try {
            List<Schedule> allByNextLessThan = scheduleRepository.findByNextLessThanAndActiveTrue(ZonedDateTime.now());
            logger.debug("fireJob Schedule size: {}", allByNextLessThan.size());

            allByNextLessThan.forEach(this::handle);
            List<ScheduleLog> allNotCompleted = scheduleLogRepository.findByNextLessThanAndCompletedFalse(ZonedDateTime.now());
            allNotCompleted.forEach(this::handleLog);
        } catch (Exception e) {
            logger.error("fire_job exception", e);
        }
    }

    @Transactional
    public void handleLog(ScheduleLog scheduleLog) {
        logger.debug("handle scheduleLog : {}", scheduleLog);
        String cronLog = scheduleLog.getSchedule().getCronLog();
        ZonedDateTime nextTimeLog = getNextTime(ZonedDateTime.now(), cronLog, scheduleLog.getCreated());
        ZonedDateTime next = scheduleLog.getNext();
        logger.debug("scheduleLog current next : {} new next time:{}", next, nextTimeLog);
        scheduleLog.setNext(nextTimeLog);
        scheduleLogRepository.save(scheduleLog);
        mailService.sendNotificationEmail(scheduleLog.getSchedule());
    }

    @Transactional
    public void handle(Schedule schedule) {
        if (!schedule.getActive()) {
            return;
        }
        logger.debug("handle schedule : {}", schedule);
        ZonedDateTime next = schedule.getNext();
        ZonedDateTime nextTime = getNextTime(schedule);
        logger.debug("handle current next : {} new next time:{}", next, nextTime);
        schedule.setNext(nextTime);
        scheduleRepository.save(schedule);
        ScheduleLog scheduleLog = new ScheduleLog(ZonedDateTime.now(), schedule);
        String cronLog = scheduleLog.getSchedule().getCronLog();
        ZonedDateTime nextTimeLog = getNextTime(ZonedDateTime.now(), cronLog, schedule.getStart());
        scheduleLog.setNext(nextTimeLog);
        scheduleLogRepository.save(scheduleLog);
        mailService.sendNotificationEmail(schedule);

    }

    private ZonedDateTime getNextTime(Schedule schedule) {
        return getNextTime(schedule.getNext(), schedule.getCron(), schedule.getStart());
    }
}
