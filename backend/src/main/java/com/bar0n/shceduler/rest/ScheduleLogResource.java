package com.bar0n.shceduler.rest;


import com.bar0n.shceduler.data.ScheduleLogRepository;
import com.bar0n.shceduler.model.ScheduleLog;
import com.bar0n.shceduler.rest.errors.BadRequestAlertException;
import com.bar0n.shceduler.rest.util.HeaderUtil;
import com.bar0n.shceduler.rest.util.PaginationUtil;
import com.bar0n.shceduler.rest.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class ScheduleLogResource {
    private final Logger log = LoggerFactory.getLogger(ScheduleLogResource.class);

    private static final String ENTITY_NAME = "scheduleLog";

    private final ScheduleLogRepository scheduleLogRepository;

    public ScheduleLogResource(ScheduleLogRepository scheduleLogRepository) {
        this.scheduleLogRepository = scheduleLogRepository;
    }

    /**
     * POST  /scheduleLog : Create a new scheduleLog.
     *
     * @param scheduleLog the scheduleLog to create
     * @return the ResponseEntity with status 201 (Created) and with body the new scheduleLog, or with status 400 (Bad Request) if the scheduleLog has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/scheduleLogs")
    public ResponseEntity<ScheduleLog> createScheduleLog(@RequestBody ScheduleLog scheduleLog) throws URISyntaxException {
        log.debug("REST request to save ScheduleLog : {}", scheduleLog);
        if (scheduleLog.getId() != null) {
            throw new BadRequestAlertException("A new scheduleLog cannot already have an ID", ENTITY_NAME, "idexists");
        }

        ScheduleLog result = scheduleLogRepository.save(scheduleLog);
        return ResponseEntity.created(new URI("/api/scheduleLog/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /scheduleLog : Updates an existing scheduleLog.
     *
     * @param scheduleLog the scheduleLog to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated scheduleLog,
     * or with status 400 (Bad Request) if the scheduleLog is not valid,
     * or with status 500 (Internal Server Error) if the scheduleLog couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping(value = "/scheduleLogs",  produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScheduleLog> updateScheduleLog(@RequestBody ScheduleLog scheduleLog) throws URISyntaxException {
        log.debug("REST request to update ScheduleLog : {}", scheduleLog);
        if (scheduleLog.getId() == null) {
            return createScheduleLog(scheduleLog);
        }

        ScheduleLog result = scheduleLogRepository.save(scheduleLog);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, scheduleLog.getId().toString()))
                .body(result);
    }

    /**
     * GET  /scheduleLog : get all the schedules.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of schedules in body
     */
    @GetMapping("/scheduleLogs")
    public ResponseEntity<List<ScheduleLog>> getAllScheduleLogs(Pageable pageable) {
        log.debug("REST request to get a page of ScheduleLogs");
        Page<ScheduleLog> page = scheduleLogRepository.findAllByOrderByIdDesc(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/scheduleLog");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /scheduleLog/:id : get the "id" scheduleLog.
     *
     * @param id the id of the scheduleLog to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the scheduleLog, or with status 404 (Not Found)
     */
    @GetMapping("/scheduleLogs/{id}")
    public ResponseEntity<ScheduleLog> getScheduleLog(@PathVariable Long id) {
        log.debug("REST request to get ScheduleLog : {}", id);
        Optional<ScheduleLog> byId = scheduleLogRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(byId);
    }

    /**
     * DELETE  /scheduleLog/:id : delete the "id" scheduleLog.
     *
     * @param id the id of the scheduleLog to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/scheduleLogs/{id}")
    public ResponseEntity<Void> deleteScheduleLog(@PathVariable Long id) {
        log.debug("REST request to delete ScheduleLog : {}", id);
        Optional<ScheduleLog> byId = scheduleLogRepository.findById(id);
        byId.ifPresent(scheduleLogRepository::delete);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}