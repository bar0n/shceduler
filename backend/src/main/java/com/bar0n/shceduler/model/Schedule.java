package com.bar0n.shceduler.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Created by dbaron
 */
@Entity
public class Schedule  implements Serializable {
    @Id
    @SequenceGenerator(name = "schedule_generator", sequenceName = "schedule_sequence", initialValue = 1)
    @GeneratedValue(generator = "schedule_generator")
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column
    private String cron;
    @Column
    private ZonedDateTime start;
    @Column
    private ZonedDateTime next;

    public Schedule() {
    }

    public Schedule(Long id, String name, String cron, ZonedDateTime start, ZonedDateTime next) {
        this.id = id;
        this.name = name;
        this.cron = cron;
        this.start = start;
        this.next = next;
    }

    public Schedule(String name, String cron, ZonedDateTime start, ZonedDateTime next) {
        this.name = name;
        this.cron = cron;
        this.start = start;
        this.next = next;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getNext() {
        return next;
    }

    public void setNext(ZonedDateTime next) {
        this.next = next;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return Objects.equals(id, schedule.id) &&
                Objects.equals(name, schedule.name) &&
                Objects.equals(cron, schedule.cron) &&
                Objects.equals(start, schedule.start) &&
                Objects.equals(next, schedule.next);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, cron, start, next);
    }

    @Override
    public String toString() {
        return "Schedule{" + "id=" + id +
                ", name='" + name + '\'' +
                ", cron='" + cron + '\'' +
                ", start=" + start +
                ", next=" + next +
                '}';
    }
}
