package com.bar0n.shceduler.rest;


import com.bar0n.shceduler.data.ScheduleRepository;
import com.bar0n.shceduler.model.Schedule;
import com.bar0n.shceduler.services.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class EventsResource {
    private final Logger log = LoggerFactory.getLogger(EventsResource.class);


    private final ScheduleService scheduleService;
    private final ScheduleRepository scheduleRepository;

    public EventsResource(ScheduleService scheduleService, ScheduleRepository scheduleRepository) {

        this.scheduleService = scheduleService;
        this.scheduleRepository = scheduleRepository;
    }


    /**
     * GET  /schedules : get all the schedules.
     *
     * @param param the param
     * @return the ResponseEntity with status 200 (OK) and the list of schedules in body
     */
    @PostMapping("/events")
    public List<ZonedDateTime> getEvents(@RequestBody Param param) {
        log.debug("REST request to get  Events");
        List<ZonedDateTime> datesBetween = scheduleService.getDatesBetween(param.getStart()
                .atStartOfDay(ZoneId.of("Europe/Helsinki")), param.getEnd().atStartOfDay(ZoneId.of("Europe/Helsinki")), param.getCron());
        return datesBetween;
    }

    @PostMapping("/events/all")
    public List<EventResult> getAllEvents(@RequestBody Param param) {
        log.debug("REST request to get  Events");
        List<Schedule> byfindByIdIn = scheduleRepository.findByIdIn(param.getIds());
        List<EventResult> collect1 = byfindByIdIn.stream().flatMap(x -> {
            List<ZonedDateTime> datesBetween = scheduleService.getDatesBetween(param.getStart()
                    .atStartOfDay(ZoneId.of("Europe/Helsinki")), param.getEnd().atStartOfDay(ZoneId.of("Europe/Helsinki")), x.getCron());
            return datesBetween.stream().map(y -> new EventResult(y, x.getName()));

        }).collect(Collectors.toList());
        return collect1;
    }

}

class EventResult {
    private ZonedDateTime start;
    private String title;

    public EventResult(ZonedDateTime start, String title) {
        this.start = start;
        this.title = title;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

class Param {
    private LocalDate start;
    private LocalDate end;
    private String cron;
    private List<Long> ids;

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}