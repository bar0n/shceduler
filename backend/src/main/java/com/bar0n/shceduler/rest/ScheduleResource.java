package com.bar0n.shceduler.rest;


import com.bar0n.shceduler.data.ScheduleRepository;
import com.bar0n.shceduler.model.Schedule;
import com.bar0n.shceduler.rest.errors.BadRequestAlertException;
import com.bar0n.shceduler.rest.util.HeaderUtil;
import com.bar0n.shceduler.rest.util.PaginationUtil;
import com.bar0n.shceduler.rest.util.ResponseUtil;
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
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class ScheduleResource {
    private final Logger log = LoggerFactory.getLogger(ScheduleResource.class);

    private static final String ENTITY_NAME = "schedule";

    private final ScheduleRepository scheduleRepository;

    public ScheduleResource(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
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
        Page<Schedule> page = scheduleRepository.findAll(pageable);
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
        byId.ifPresent(scheduleRepository::delete);

        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
 /*   private static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

    private final ScheduleService scheduleService; //Service which will do all data retrieval/manipulation work

    @Autowired
    public RestApiController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // -------------------Retrieve All Schedules---------------------------------------------

    @RequestMapping(value = "/schedule2/", method = RequestMethod.GET)
    public ResponseEntity listAllSchedules() {
        List<Schedule> schedules = scheduleService.findAllSchedules();
        if (schedules.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
            // You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    // -------------------Retrieve Single Schedule------------------------------------------

    @RequestMapping(value = "/schedule2/{id}", method = RequestMethod.GET)
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

    @RequestMapping(value = "/schedule2/", method = RequestMethod.POST)
    public ResponseEntity<?> createSchedule(@RequestBody Schedule schedule, UriComponentsBuilder ucBuilder) {
        logger.info("Creating Schedule : {}", schedule);

        if (scheduleService.isScheduleExist(schedule)) {
            logger.error("Unable to create. A Schedule with name {} already exist", schedule.getName());
            return new ResponseEntity(new CustomErrorType("Unable to create. A Schedule with name " +

                    schedule.getName() + " already exist."), HttpStatus.CONFLICT);
        }
        scheduleService.saveSchedule(schedule);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/schedule2/{id}").buildAndExpand(schedule.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    // ------------------- Update a Schedule ------------------------------------------------

    @RequestMapping(value = "/schedule2/{id}", method = RequestMethod.PUT)
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

    @RequestMapping(value = "/schedule2/{id}", method = RequestMethod.DELETE)
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

    @RequestMapping(value = "/schedule2/", method = RequestMethod.DELETE)
    public ResponseEntity<Schedule> deleteAllSchedules() {
        logger.info("Deleting All Schedules");

        scheduleService.deleteAllSchedules();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }*/

}