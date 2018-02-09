package com.bar0n.shceduler.rest;


import com.bar0n.shceduler.errors.CustomErrorType;
import com.bar0n.shceduler.model.Schedule;
import com.bar0n.shceduler.services.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


@RestController
@RequestMapping("/api")
public class RestApiController {

    private static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

    private final ScheduleService scheduleService; //Service which will do all data retrieval/manipulation work

    @Autowired
    public RestApiController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // -------------------Retrieve All Schedules---------------------------------------------

    @RequestMapping(value = "/schedule/", method = RequestMethod.GET)
    public ResponseEntity listAllSchedules() {
        List<Schedule> schedules = scheduleService.findAllSchedules();
        if (schedules.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
            // You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    // -------------------Retrieve Single Schedule------------------------------------------

    @RequestMapping(value = "/schedule/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getSchedule(@PathVariable("id") long id) {
        logger.info("Fetching Schedule with id {}", id);
        Schedule schedule = scheduleService.findById(id);
        if (schedule == null) {
            logger.error("Schedule with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Schedule with id " + id
                    + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }

    // -------------------Create a Schedule-------------------------------------------

    @RequestMapping(value = "/schedule/", method = RequestMethod.POST)
    public ResponseEntity<?> createSchedule(@RequestBody Schedule schedule, UriComponentsBuilder ucBuilder) {
        logger.info("Creating Schedule : {}", schedule);

        if (scheduleService.isScheduleExist(schedule)) {
            logger.error("Unable to create. A Schedule with name {} already exist", schedule.getName());
            return new ResponseEntity(new CustomErrorType("Unable to create. A Schedule with name " +

                    schedule.getName() + " already exist."), HttpStatus.CONFLICT);
        }
        scheduleService.saveSchedule(schedule);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/schedule/{id}").buildAndExpand(schedule.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    // ------------------- Update a Schedule ------------------------------------------------

    @RequestMapping(value = "/schedule/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateSchedule(@PathVariable("id") long id, @RequestBody Schedule schedule) {
        logger.info("Updating Schedule with id {}", id);

        Schedule currentSchedule = scheduleService.findById(id);

        if (currentSchedule == null) {
            logger.error("Unable to update. Schedule with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Unable to upate. Schedule with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }

        currentSchedule.setName(schedule.getName());
        currentSchedule.setCron(schedule.getCron());
        currentSchedule.setNext(schedule.getNext());
        currentSchedule.setStart(schedule.getStart());


        scheduleService.updateSchedule(currentSchedule);
        return new ResponseEntity<>(currentSchedule, HttpStatus.OK);
    }

    // ------------------- Delete a Schedule-----------------------------------------

    @RequestMapping(value = "/schedule/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteSchedule(@PathVariable("id") long id) {
        logger.info("Fetching & Deleting Schedule with id {}", id);

        Schedule schedule = scheduleService.findById(id);
        if (schedule == null) {
            logger.error("Unable to delete. Schedule with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Unable to delete. Schedule with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        scheduleService.deleteScheduleById(id);
        return new ResponseEntity<Schedule>(HttpStatus.NO_CONTENT);
    }

    // ------------------- Delete All Schedules-----------------------------

    @RequestMapping(value = "/schedule/", method = RequestMethod.DELETE)
    public ResponseEntity<Schedule> deleteAllSchedules() {
        logger.info("Deleting All Schedules");

        scheduleService.deleteAllSchedules();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}