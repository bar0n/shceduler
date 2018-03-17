package com.bar0n.shceduler.data;

import com.bar0n.shceduler.model.Schedule;
import com.bar0n.shceduler.model.ScheduleLog;
import com.bar0n.shceduler.services.ScheduleService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dbaron
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceIntegrationTests {
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    ScheduleLogRepository scheduleLogRepository;

    @Test
    //@Rollback(true)
    public void manyToOneTest() {
        LocalDateTime now = LocalDateTime.now().minusDays(1);
        Schedule schedule = new Schedule("name1", "0 */10 * ? * *", now, now);
        schedule.setCronLog("0 */10 * ? * *");
        scheduleRepository.save(schedule);
        scheduleService.handle(schedule);
        scheduleService.handle(schedule);

    }

}
