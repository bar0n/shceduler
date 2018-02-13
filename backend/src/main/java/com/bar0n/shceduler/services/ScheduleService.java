package com.bar0n.shceduler.services;

import com.bar0n.shceduler.data.ScheduleLogRepository;
import com.bar0n.shceduler.data.ScheduleRepository;
import com.bar0n.shceduler.model.Schedule;
import com.bar0n.shceduler.model.ScheduleLog;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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

    public ZonedDateTime getNextTime(ZonedDateTime time, String cronExpressionTxt) {
        CronExpression cronExpression = null;
        try {
            cronExpression = new CronExpression(cronExpressionTxt);
        } catch (ParseException e) {
            throw new RuntimeException("parse error", e);
        }
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime t = (now.compareTo(time) > 0) ? now : time;
        Date from = Date.from(t.toInstant());
        Date nextValidTimeAfter = cronExpression.getNextValidTimeAfter(from);
        ZoneId zone = time.getZone();
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(nextValidTimeAfter.toInstant(), zone);
        logger.debug("getNextTime time: {}, now:{}, max(time,now):{}, nextTime:{}", time, now, t, zonedDateTime);
        return zonedDateTime;
    }

    @Transactional
    public void fireJob() {
        try {
            List<Schedule> allByNextLessThan = scheduleRepository.findAllByNextLessThan(ZonedDateTime.now());
            logger.debug("fireJob Schedule size: {}", allByNextLessThan.size());

            allByNextLessThan.forEach(this::handle);

        }
        catch (Exception e){
            logger.error("fire_job exception",e);
        }
    }

    @Transactional
    public void handle(Schedule schedule) {
        logger.debug("handle schedule : {}", schedule);
        ZonedDateTime next = schedule.getNext();
        ZonedDateTime nextTime = getNextTime(schedule);
        logger.debug("handle current next : {} new next time:{}", next, nextTime);
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
