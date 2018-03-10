package com.bar0n.shceduler.rest;


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


@RestController
@RequestMapping("/api")
public class EventsResource {
    private final Logger log = LoggerFactory.getLogger(EventsResource.class);


    private final ScheduleService scheduleService;

    public EventsResource(ScheduleService scheduleService) {

        this.scheduleService = scheduleService;
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


}

class Param {
    private LocalDate start;
    private LocalDate end;
    private String cron;
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



}