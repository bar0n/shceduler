package com.bar0n.shceduler.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Objects;

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
    @JsonManagedReference
    //@JoinColumn(name = "schedule_id", insertable = false, updatable = false)
    private Schedule schedule;
    @Column
    private Boolean completed = false;

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

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleLog that = (ScheduleLog) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(created, that.created) &&
                Objects.equals(schedule, that.schedule) &&
                Objects.equals(completed, that.completed);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, created, schedule, completed);
    }

    @Override
    public String toString() {
        return "ScheduleLog{" +
                "id=" + id +
                ", created=" + created +
                ", schedule=" + schedule +
                '}';
    }
}
