package com.bar0n.shceduler.services;

import com.bar0n.shceduler.model.Schedule;

import java.util.List;

/**
 * Created by dbaron
 */
public interface ScheduleService {
    Schedule findById(long id);

    List<Schedule> findAllSchedules();

    boolean isScheduleExist(Schedule schedule);

    void saveSchedule(Schedule schedule);

    void deleteScheduleById(long id);

    void updateSchedule(Schedule currentSchedule);

    void deleteAllSchedules();
}
