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
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    synchronized public List<LocalDateTime> getDatesBetween(LocalDateTime start, LocalDateTime stop, String cronExpressionTxt, LocalDateTime fromDate) {
        MyCronExpression cronExpression;
        fromDate = start.compareTo(fromDate) < 0 ? start : fromDate;
        try {
            cronExpression = new MyCronExpression(cronExpressionTxt);
        } catch (ParseException e) {
            throw new RuntimeException("parse error", e);
        }
        return cronExpression.getDatesBetween(start, stop, fromDate);

    }

    public LocalDateTime getNextTime(LocalDateTime time, String cronExpressionTxt, LocalDateTime start) {
        MyCronExpression cronExpression = null;
        try {
            cronExpression = new MyCronExpression(cronExpressionTxt);
        } catch (ParseException e) {
            throw new RuntimeException("parse error", e);
        }
        LocalDateTime now = DateUtils.now();
        LocalDateTime from = (now.compareTo(time) > 0) ? now : time;
        LocalDateTime nextValidTimeAfter = cronExpression.getNextValidTimeAfter(from, start);
        logger.debug("getNextTime time: {}, now:{}, max(time,now):{}, nextTime:{}", time, now, from, nextValidTimeAfter);
        return nextValidTimeAfter;
    }

    @Transactional
    public void fireJob() {
        try {
            List<Schedule> allByNextLessThan = scheduleRepository.findByNextLessThanAndActiveTrue(DateUtils.now());
            logger.debug("fireJob Schedule size: {}", allByNextLessThan.size());

            allByNextLessThan.forEach(this::handle);
            List<ScheduleLog> allNotCompleted = scheduleLogRepository.findByNextLessThanAndCompletedFalse(DateUtils.now());
            allNotCompleted.forEach(this::handleLog);
        } catch (Exception e) {
            logger.error("fire_job exception", e);
        }
    }

    @Transactional
    public void handleLog(ScheduleLog scheduleLog) {
        logger.debug("handle scheduleLog : {}", scheduleLog);
        String cronLog = scheduleLog.getSchedule().getCronLog();
        LocalDateTime nextTimeLog = getNextTime(DateUtils.now(), cronLog, scheduleLog.getCreated());
        LocalDateTime next = scheduleLog.getNext();
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
        LocalDateTime next = schedule.getNext();
        LocalDateTime nextTime = getNextTime(schedule);
        logger.debug("handle current next : {} new next time:{}", next, nextTime);
        schedule.setNext(nextTime);
        scheduleRepository.save(schedule);
        ScheduleLog scheduleLog = new ScheduleLog(DateUtils.now(), schedule);
        String cronLog = scheduleLog.getSchedule().getCronLog();
        LocalDateTime nextTimeLog = getNextTime(DateUtils.now(), cronLog, schedule.getStart());
        scheduleLog.setNext(nextTimeLog);
        scheduleLogRepository.save(scheduleLog);
        mailService.sendNotificationEmail(schedule);

    }

    private LocalDateTime getNextTime(Schedule schedule) {
        return getNextTime(schedule.getNext(), schedule.getCron(), schedule.getStart());
    }
}
