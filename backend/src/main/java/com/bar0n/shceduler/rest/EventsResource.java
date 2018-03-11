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

import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
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
/*    @PostMapping("/events")
    public List<ZonedDateTime> getEvents(@RequestBody Param param) {
        log.debug("REST request to get  Events");
        List<ZonedDateTime> datesBetween = scheduleService.getDatesBetween(param.getStart()
                .atStartOfDay(ZoneId.of("Europe/Helsinki")), param.getEnd().atStartOfDay(ZoneId.of("Europe/Helsinki")), param.getCron());
        return datesBetween;
    }*/
    @PostMapping("/events/all")
    public List<EventResult> getAllEvents(@RequestBody Param param) {
        log.debug("REST request to get  Events");
        List<Schedule> byfindByIdIn = scheduleRepository.findByIdIn(param.getIds());
        List<EventResult> collect1 = byfindByIdIn.stream().flatMap(x -> {
            List<ZonedDateTime> datesBetween = scheduleService.getDatesBetween(param.getStart()
                    .atStartOfDay(ZoneId.of("Europe/Helsinki")), param.getEnd().atStartOfDay(ZoneId.of("Europe/Helsinki")), x.getCron(), x.getStart());
            return datesBetween.stream().map(getZonedDateTimeEventResultFunction(x));

        }).collect(Collectors.toList());
        return collect1;
    }

    private Function<ZonedDateTime, EventResult> getZonedDateTimeEventResultFunction(Schedule x) {
        return y -> {
            EventResult eventResult = new EventResult(y, x.getName());
            eventResult.end = y.plusHours(1);
            eventResult.allDay = false;
            eventResult.id = "" + x.getId() + "$" + x.getStart();
            int abs = Math.abs(x.getId().intValue() % 256);

            String indexcolor = indexcolors[abs];
            String[] split = indexcolor.replaceAll("#", "").split("(?<=\\G.{2})");
            List<Integer> collect = Arrays.stream(split).map(col -> Integer.parseInt(col, 16)).collect(Collectors.toList());
            String textColor = "#ffffff";
            if (!collect.isEmpty()) {
                Integer red = collect.get(0);
                Integer green = collect.get(0);
                Integer blue = collect.get(0);
                if ((red * 0.299 + green * 0.587 + blue * 0.114) > 186) {
                    textColor = "#000000";
                }
            }
            eventResult.url = "/scheduleEdit/" + x.getId();
            eventResult.color = new String[]{indexcolor};
            eventResult.textColor = new String[]{textColor};
            return eventResult;
        };
    }

    private static final String[] indexcolors = new String[]{
            "#000000", "#FFFF00", "#1CE6FF", "#FF34FF", "#FF4A46", "#008941", "#006FA6", "#A30059",
            "#FFDBE5", "#7A4900", "#0000A6", "#63FFAC", "#B79762", "#004D43", "#8FB0FF", "#997D87",
            "#5A0007", "#809693", "#FEFFE6", "#1B4400", "#4FC601", "#3B5DFF", "#4A3B53", "#FF2F80",
            "#61615A", "#BA0900", "#6B7900", "#00C2A0", "#FFAA92", "#FF90C9", "#B903AA", "#D16100",
            "#DDEFFF", "#000035", "#7B4F4B", "#A1C299", "#300018", "#0AA6D8", "#013349", "#00846F",
            "#372101", "#FFB500", "#C2FFED", "#A079BF", "#CC0744", "#C0B9B2", "#C2FF99", "#001E09",
            "#00489C", "#6F0062", "#0CBD66", "#EEC3FF", "#456D75", "#B77B68", "#7A87A1", "#788D66",
            "#885578", "#FAD09F", "#FF8A9A", "#D157A0", "#BEC459", "#456648", "#0086ED", "#886F4C",

            "#34362D", "#B4A8BD", "#00A6AA", "#452C2C", "#636375", "#A3C8C9", "#FF913F", "#938A81",
            "#575329", "#00FECF", "#B05B6F", "#8CD0FF", "#3B9700", "#04F757", "#C8A1A1", "#1E6E00",
            "#7900D7", "#A77500", "#6367A9", "#A05837", "#6B002C", "#772600", "#D790FF", "#9B9700",
            "#549E79", "#FFF69F", "#201625", "#72418F", "#BC23FF", "#99ADC0", "#3A2465", "#922329",
            "#5B4534", "#FDE8DC", "#404E55", "#0089A3", "#CB7E98", "#A4E804", "#324E72", "#6A3A4C",
            "#83AB58", "#001C1E", "#D1F7CE", "#004B28", "#C8D0F6", "#A3A489", "#806C66", "#222800",
            "#BF5650", "#E83000", "#66796D", "#DA007C", "#FF1A59", "#8ADBB4", "#1E0200", "#5B4E51",
            "#C895C5", "#320033", "#FF6832", "#66E1D3", "#CFCDAC", "#D0AC94", "#7ED379", "#012C58"
    };
}

class EventResult {
    public String id; //	 String/Integer. Optional Uniquely identifies the given event. Different instances of repeating events should all have the same id.
    public String title;//. Required. The text on an event's element
    public Boolean allDay;//		true or false. Optional. Whether an event occurs at a specific time-of-day. This property affects whether an event's time is shown. Also, in the agenda views, determines if it is displayed in the "all-day" section. If this value is not explicitly specified, allDayDefault will be used if it is defined. If all else fails, FullCalendar will try to guess. If either the start or end value has a "T" as part of the ISO8601 date  String, allDay will become false. Otherwise, it will be true. Don't include quotes around your true/false. This value is a  Boolean, not a  String!
    public ZonedDateTime start;//		The date/time an event begins. Required. A Moment-ish input, like an ISO8601  String. Throughout the API this will become a real Moment object.
    public ZonedDateTime end;//	The exclusive date/time an event ends. Optional. A Moment-ish input, like an ISO8601  String. Throughout the API this will become a real Moment object. It is the moment immediately after the event has ended. For example, if the last full day of an event is Thursday, the exclusive end of the event will be 00 00 00 on Friday!
    public String url;//. Optional. A URL that will be visited when this event is clicked by the user. For more information on controlling this behavior, see the eventClick callback.
    public String[] className;//		 String/Array. Optional. A CSS class (or array of classes) that will be attached to this event's element.
    public Boolean editable;//		true or false. Optional. Overrides the master editable option for this single event.
    public Boolean startEditable;//	true or false. Optional. Overrides the master eventStartEditable option for this single event.
    public Boolean durationEditable;//	true or false. Optional. Overrides the master eventDurationEditable option for this single event.
    public Boolean resourceEditable;//	true or false. Optional. Overrides the master eventResourceEditable option for this single event.
    public String rendering;//	Allows 	 alternate rendering of the event, like background events. Can be empty, "background", or "inverse-background"
    public Boolean overlap;//		true or false. Optional. Overrides the master eventOverlap option for this single event. If false, prevents this event from being dragged/resized over other events. Also prevents other events from being dragged/resized over this event.
    public String constraint;//		an event ID, "businessHours", object. Optional. Overrides the master eventConstraint option for this single event.
    public Object source;//		Event Source Object. Automatically populated. A reference to the event source that this event came from.
    public String[] color;//	Sets an event's background and border color just like the calendar-wide eventColor option.
    public String[] backgroundColor;//	Sets an event's background color just like the calendar-wide eventBackgroundColor option.
    public String[] borderColor;//	Sets an event's border color just like the the calendar-wide eventBorderColor option.
    public String[] textColor;

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