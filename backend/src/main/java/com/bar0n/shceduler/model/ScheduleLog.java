package com.bar0n.shceduler.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.time.ZonedDateTime;

/**
 * Created by dbaron
 */
@Entity
public class ScheduleLog {
    @Id
    @SequenceGenerator(name = "schedule_generator", sequenceName = "schedule_log_sequence", initialValue = 1)
    @GeneratedValue(generator = "schedule_generator")
    private Long id;
    @Column
    private ZonedDateTime created;

    @ManyToOne(optional = false)
    @NaturalId
    private Schedule schedule;


    public ScheduleLog(ZonedDateTime created, Schedule schedule) {
        this.created = created;
        this.schedule = schedule;
    }

    public ScheduleLog() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
