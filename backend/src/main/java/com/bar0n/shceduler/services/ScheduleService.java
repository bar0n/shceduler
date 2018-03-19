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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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


    public LocalDateTime getNextTime(LocalDateTime from, String cronExpressionTxt, LocalDateTime start) {
        MyCronExpression cronExpression = null;
        try {
            cronExpression = new MyCronExpression(cronExpressionTxt);
        } catch (ParseException e) {
            throw new RuntimeException("parse error", e);
        }

        return cronExpression.getNextValidTimeAfter(from, start);
    }

    public Schedule save(Schedule schedule) {
        schedule.setModified(DateUtils.now());
        return scheduleRepository.save(schedule);
    }

    public ScheduleLog save(ScheduleLog scheduleLog) {
        scheduleLog.setModified(DateUtils.now());
        return scheduleLogRepository.save(scheduleLog);
    }

    public LocalDateTime getNextTime(String cron, LocalDateTime start) {
        LocalDateTime now = DateUtils.now();
        LocalDateTime nextTime = getNextTime(now, cron, start);
        logger.debug("getNextTime now:{}  cron:{} start:{} = result:{}", now, cron, start, nextTime);
        return nextTime;
    }

    @Transactional
    public void fireJob() {
        try {
            List<Schedule> allByNextLessThan = scheduleRepository.findByNextLessThanAndActiveTrue(DateUtils.now());
            logger.debug("fireJob Schedule size: {}", allByNextLessThan.size());
            allByNextLessThan.forEach(this::handle);
            List<ScheduleLog> allNotCompleted = scheduleLogRepository.findByNextLessThanAndCompletedFalse(DateUtils.now());
            handleLogs(allNotCompleted);

        } catch (Exception e) {
            logger.error("fire_job exception", e);
        }
    }

    private void handleLogs(List<ScheduleLog> allNotCompleted) {
        Set<Long> scheduleIds = allNotCompleted.stream().map(x -> x.getSchedule().getId()).collect(Collectors.toSet());
        List<Schedule> byIdIn = scheduleRepository.findByIdIn(new ArrayList<>(scheduleIds));
        byIdIn.forEach(s -> {
                    List<ScheduleLog> collect = allNotCompleted.stream().filter(x -> Objects.equals(x.getSchedule().getId(), s.getId())).collect(Collectors.toList());
                    collect.forEach(this::updateNextTime);
                    collect.stream().findFirst().ifPresent(this::sendLogEmail);
                }
        );

    }


    private void sendLogEmail(ScheduleLog scheduleLog) {
        mailService.sendNotificationReminderEmail(scheduleLog.getSchedule());
    }

    private void updateNextTime(ScheduleLog scheduleLog) {
        logger.debug("handle scheduleLog : {}", scheduleLog);
        String cronLog = scheduleLog.getSchedule().getCronLog();
        LocalDateTime nextTimeLog = getNextTime(cronLog, scheduleLog.getCreated());
        LocalDateTime next = scheduleLog.getNext();
        logger.debug("scheduleLog current next : {} new next time:{}", next, nextTimeLog);
        scheduleLog.setNext(nextTimeLog);
        save(scheduleLog);
    }

    @Transactional
    public void handle(Schedule schedule) {
        if (!schedule.getActive()) {
            return;
        }
        logger.debug("handle schedule : {}", schedule);
        LocalDateTime next = schedule.getNext();
        LocalDateTime nextTime = getNextTime(schedule.getCron(), schedule.getStart());
        logger.debug("handle current next : {} new next time:{}", next, nextTime);
        schedule.setNext(nextTime);
        save(schedule);
        ScheduleLog scheduleLog = new ScheduleLog(DateUtils.now(), schedule);
        String cronLog = scheduleLog.getSchedule().getCronLog();
        LocalDateTime nextTimeLog = getNextTime(cronLog, schedule.getStart());
        scheduleLog.setNext(nextTimeLog);
        save(scheduleLog);
        mailService.sendNotificationEmail(schedule);
    }
}
