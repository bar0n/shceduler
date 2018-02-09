package com.bar0n.shceduler.data;

import com.bar0n.shceduler.model.Schedule;
import com.bar0n.shceduler.model.ScheduleLog;
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

import static org.assertj.core.api.Assertions.assertThat;
/**
 * Created by dbaron
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoryIntegrationTests {
    @Autowired
    ScheduleRestResource scheduleRestResource;
    @Autowired
    ScheduleLogRestResource scheduleLogRestResource;

    @Test
    @Rollback(false)
    public void findsFirstPageOf() {

        Page<ScheduleLog> scheduleLogs = this.scheduleLogRestResource.findAll(PageRequest.of(0, 10));
        assertThat(scheduleLogs.getTotalElements()).isEqualTo(0L);

        Page<Schedule> schedules = this.scheduleRestResource.findAll(PageRequest.of(0, 10));
        assertThat(schedules.getTotalElements()).isEqualTo(0L);
        Schedule schedule = new Schedule("name", "cron", ZonedDateTime.now(), ZonedDateTime.now());
        scheduleRestResource.save(schedule);

        ScheduleLog scheduleLog = new ScheduleLog(ZonedDateTime.now(), schedule);
        scheduleLogRestResource.save(scheduleLog);
        Assert.assertNotNull(schedule.getId());
        Assert.assertNotNull(scheduleLog.getId());
        System.out.println(scheduleLog);

    }

   /* @scheduleLog
    public void findByNameAndCountry() {
        Schedule city = this.scheduleRestResource.findByNameAndCountryAllIgnoringCase("Melbourne",
                "Australia");
        assertThat(city).isNotNull();
        assertThat(city.getName()).isEqualTo("Melbourne");
    }

    @Test
    public void findContaining() {
        Page<Schedule> cities = this.scheduleRestResource
                .findByNameContainingAndCountryContainingAllIgnoringCase("", "UK",
                        PageRequest.of(0, 10));
        assertThat(cities.getTotalElements()).isEqualTo(3L);
    }*/
}
