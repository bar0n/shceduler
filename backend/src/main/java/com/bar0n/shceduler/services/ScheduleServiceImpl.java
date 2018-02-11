package com.bar0n.shceduler.services;

import com.bar0n.shceduler.model.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import com.bar0n.shceduler.data.ScheduleRepository;
/**
 * Created by dbaron
 */
@Service("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {

    private static List<Schedule> schedules;

    public Schedule findById(long id) {
        for (Schedule schedule : schedules) {
            if (schedule.getId() == id) {
                return schedule;
            }
        }
        return null;
    }

    public Schedule findByName(String name) {
        for (Schedule schedule : schedules) {
            if (schedule.getName().equalsIgnoreCase(name)) {
                return schedule;
            }
        }
        return null;
    }

    public List<Schedule> findAllSchedules() {
        return schedules;
    }

    public boolean isScheduleExist(Schedule schedule) {
        return findByName(schedule.getName()) != null;
    }

    public void saveSchedule(Schedule schedule) {
        schedule.setId(counter.incrementAndGet());
        schedules.add(schedule);
    }

    public void deleteScheduleById(long id) {
        schedules.removeIf(schedule -> schedule.getId() == id);
    }

    public void updateSchedule(Schedule schedule) {
        int index = schedules.indexOf(schedule);
        schedules.set(index, schedule);
    }

    public void deleteAllSchedules() {
        schedules.clear();
    }

    private static final AtomicLong counter = new AtomicLong();

    static {
        schedules = populateDummySchedules();
    }

    private static List<Schedule> populateDummySchedules() {
        List<Schedule> schedules = new ArrayList<>();
        ZonedDateTime start = ZonedDateTime.now();
        schedules.add(new Schedule(counter.incrementAndGet(), "Sam", "", start, start));
        schedules.add(new Schedule(counter.incrementAndGet(), "Tom", "", start, start));
        schedules.add(new Schedule(counter.incrementAndGet(), "Jerome", "", start, start));
        schedules.add(new Schedule(counter.incrementAndGet(), "Silvia", "", start, start));
        return schedules;
    }
}
