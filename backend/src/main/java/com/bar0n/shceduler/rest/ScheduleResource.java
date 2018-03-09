package com.bar0n.shceduler.rest;


import com.bar0n.shceduler.data.ScheduleLogRepository;
import com.bar0n.shceduler.data.ScheduleRepository;
import com.bar0n.shceduler.model.Schedule;
import com.bar0n.shceduler.rest.errors.BadRequestAlertException;
import com.bar0n.shceduler.rest.util.HeaderUtil;
import com.bar0n.shceduler.rest.util.PaginationUtil;
import com.bar0n.shceduler.rest.util.ResponseUtil;
import com.bar0n.shceduler.services.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class ScheduleResource {
    private final Logger log = LoggerFactory.getLogger(ScheduleResource.class);

    private static final String ENTITY_NAME = "schedule";

    private final ScheduleRepository scheduleRepository;
    private final ScheduleLogRepository scheduleLogRepository;
    private final ScheduleService scheduleService;

    public ScheduleResource(ScheduleRepository scheduleRepository, ScheduleLogRepository scheduleLogRepository, ScheduleService scheduleService) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleLogRepository = scheduleLogRepository;
        this.scheduleService = scheduleService;
    }

    /**
     * POST  /schedules : Create a new schedule.
     *
     * @param schedule the schedule to create
     * @return the ResponseEntity with status 201 (Created) and with body the new schedule, or with status 400 (Bad Request) if the schedule has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/schedules")
    public ResponseEntity<Schedule> createSchedule(@RequestBody Schedule schedule) throws URISyntaxException {
        log.debug("REST request to save Schedule : {}", schedule);
        if (schedule.getId() != null) {
            throw new BadRequestAlertException("A new schedule cannot already have an ID", ENTITY_NAME, "idexists");
        }

        ZonedDateTime nextTime = scheduleService.getNextTime(schedule.getStart(), schedule.getCron());
        ZonedDateTime nextLog = scheduleService.getNextTime(schedule.getStart(), schedule.getCron());
        schedule.setNext(nextTime);
        Schedule result = scheduleRepository.save(schedule);
        return ResponseEntity.created(new URI("/api/schedules/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /schedules : Updates an existing schedule.
     *
     * @param schedule the schedule to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated schedule,
     * or with status 400 (Bad Request) if the schedule is not valid,
     * or with status 500 (Internal Server Error) if the schedule couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/schedules")
    public ResponseEntity<Schedule> updateSchedule(@RequestBody Schedule schedule) throws URISyntaxException {
        log.debug("REST request to update Schedule : {}", schedule);
        if (schedule.getId() == null) {
            return createSchedule(schedule);
        }

        ZonedDateTime nextTime = scheduleService.getNextTime(schedule.getStart(), schedule.getCron());
        schedule.setNext(nextTime);
        Schedule result = scheduleRepository.save(schedule);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, schedule.getId().toString()))
                .body(result);
    }

    /**
     * GET  /schedules : get all the schedules.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of schedules in body
     */
    @GetMapping("/schedules")
    public ResponseEntity<List<Schedule>> getAllSchedules(Pageable pageable) {
        log.debug("REST request to get a page of Schedules");
        Page<Schedule> page = scheduleRepository.findAllByOrderByIdDesc(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/schedules");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /schedules/:id : get the "id" schedule.
     *
     * @param id the id of the schedule to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the schedule, or with status 404 (Not Found)
     */
    @GetMapping("/schedules/{id}")
    public ResponseEntity<Schedule> getSchedule(@PathVariable Long id) {
        log.debug("REST request to get Schedule : {}", id);
        Optional<Schedule> byId = scheduleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(byId);
    }

    /**
     * DELETE  /schedules/:id : delete the "id" schedule.
     *
     * @param id the id of the schedule to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/schedules/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        log.debug("REST request to delete Schedule : {}", id);
        Optional<Schedule> byId = scheduleRepository.findById(id);
        scheduleLogRepository.deleteScheduleLogsByscheduleId(id);
        byId.ifPresent(scheduleRepository::delete);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


}