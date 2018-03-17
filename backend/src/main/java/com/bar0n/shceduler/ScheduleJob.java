package com.bar0n.shceduler;

import com.bar0n.shceduler.services.DateUtils;
import com.bar0n.shceduler.services.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

/**
 * Created by dbaron
 */
@Component
public class ScheduleJob {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleJob.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final ScheduleService scheduleService;

    public ScheduleJob(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @Scheduled(fixedRate = 2 * 60 * 1000)
    public void scheduleTaskWithFixedRate() {
        logger.info("Fixed Rate Task :: Execution Time - {}", dateTimeFormatter.format(DateUtils.now()));
        scheduleService.fireJob();
    }

}
