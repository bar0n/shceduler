package com.bar0n.shceduler.data;

import com.bar0n.shceduler.model.Schedule;
import com.bar0n.shceduler.model.ScheduleLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dbaron
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoryIntegrationTests {
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    ScheduleLogRepository scheduleLogRepository;

    @Test
    @Rollback(false)
    public void findsFirstPageOf() throws JsonProcessingException {


        Page<Schedule> schedules = this.scheduleRepository.findAll(PageRequest.of(0, 10));
        assertThat(schedules.getTotalElements()).isEqualTo(0L);
        Schedule schedule = new Schedule("name", "cron", ZonedDateTime.now(), ZonedDateTime.now());
        scheduleRepository.save(schedule);

        ScheduleLog scheduleLog = new ScheduleLog(ZonedDateTime.now(), schedule);
        scheduleLogRepository.save(scheduleLog);
        ScheduleLog scheduleLog2 = new ScheduleLog(ZonedDateTime.now(), schedule);
        scheduleLogRepository.save(scheduleLog2);
        Assert.assertNotNull(schedule.getId());
        Assert.assertNotNull(scheduleLog.getId());


        Page<ScheduleLog> scheduleLogs = this.scheduleLogRepository.findAll(PageRequest.of(0, 10));
        assertThat(scheduleLogs.getTotalElements()).isEqualTo(2L);
        schedule.getScheduleLogs().add(scheduleLog);
        schedule.getScheduleLogs().add(scheduleLog2);
        System.out.println(schedule);
        System.out.println(scheduleLog);
        String result = new ObjectMapper().writeValueAsString(schedule);
        System.out.println(result);
        System.out.println("----------------------------------------------------------------");
        System.out.println(new ObjectMapper().writeValueAsString(scheduleLog2));
        System.out.println("----------------------------------------------------------------");
    }

    @Test
    @Rollback(true)
    public void findAllByNextLessThan() {
        ZonedDateTime now = ZonedDateTime.now().minusDays(1);
        Schedule schedule1 = new Schedule("name1", "cron1", now, now);
        scheduleRepository.save(schedule1);
        ZonedDateTime zonedDateTime = now.plusDays(7);
        Schedule schedule2 = new Schedule("name2", "cron2", zonedDateTime, zonedDateTime);


        List<Schedule> allByNextLessThan = scheduleRepository.findAllByNextLessThan(ZonedDateTime.now());
        System.out.println(allByNextLessThan);
    }

    @Test
    @Rollback(true)
    public void manyToOneTest() {
        ZonedDateTime now = ZonedDateTime.now().minusDays(1);
        Schedule schedule = new Schedule("name1", "cron1", now, now);
        scheduleRepository.save(schedule);
        ScheduleLog scheduleLog = new ScheduleLog(ZonedDateTime.now(), schedule);
        scheduleLog.setCompleted(true);
        scheduleLogRepository.save(scheduleLog);
        ScheduleLog scheduleLog2 = new ScheduleLog(ZonedDateTime.now(), schedule);
        scheduleLogRepository.save(scheduleLog2);

        Schedule one = scheduleRepository.getOne(schedule.getId());

        int size = one.getScheduleLogs().size();
        int i = 1;
        Assert.assertEquals(size, i);
    }

}
