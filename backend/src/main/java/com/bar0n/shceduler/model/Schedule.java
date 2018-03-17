package com.bar0n.shceduler.model;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by dbaron
 */
@Entity
@Proxy(lazy = false)
public class Schedule implements Serializable {
    @Id
    @SequenceGenerator(name = "schedule_generator", sequenceName = "schedule_sequence", initialValue = 1,allocationSize =1)
    @GeneratedValue(generator = "schedule_generator")
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column
    private String cron;
    @Column
    private String cronLog;
    @Column
    private String cronReminder;
    @Column
    private String periodTxt;
    @Column
    private String email;
    @Column
    private String description;
    @Column
    private String author;
    @Column
    private String person;
    @Column
    private LocalDateTime start;
    @Column
    private LocalDateTime next;
    @Column
    private LocalDateTime stop;
    @Column
    private LocalDateTime createdDate;
    @Column
    private Boolean active = true;
    @OneToMany(fetch = FetchType.EAGER, targetEntity = ScheduleLog.class
            , mappedBy = "schedule") //
    // @JoinColumn(name="schedule_id")
    @Where(clause = "completed = false")
    @JsonIgnore
    private List<ScheduleLog> scheduleLogs = new ArrayList<>();

    public Schedule() {
    }

    public Schedule(Long id, String name, String cron, LocalDateTime start, LocalDateTime next) {
        this.id = id;
        this.name = name;
        this.cron = cron;
        this.start = start;
        this.next = next;
    }

    public Schedule(String name, String cron, LocalDateTime start, LocalDateTime next) {
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

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getNext() {
        return next;
    }

    public void setNext(LocalDateTime next) {
        this.next = next;
    }

    public LocalDateTime getStop() {
        return stop;
    }

    public void setStop(LocalDateTime stop) {
        this.stop = stop;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

    public String getPeriodTxt() {
        return periodTxt;
    }

    public void setPeriodTxt(String periodTxt) {
        this.periodTxt = periodTxt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPerson() {
        return person;
    }

    public List<ScheduleLog> getScheduleLogs() {
        return scheduleLogs;
    }

    public String getCronReminder() {
        return cronReminder;
    }

    public void setCronReminder(String cronReminder) {
        this.cronReminder = cronReminder;
    }

    public void setScheduleLogs(List<ScheduleLog> scheduleLogs) {
        this.scheduleLogs = scheduleLogs;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public String getCronLog() {
        return cronLog;
    }

    public void setCronLog(String cronLog) {
        this.cronLog = cronLog;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }


}

