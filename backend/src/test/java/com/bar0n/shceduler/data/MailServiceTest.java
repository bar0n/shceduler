package com.bar0n.shceduler.data;

import com.bar0n.shceduler.model.Schedule;
import com.bar0n.shceduler.services.MailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.bar0n.shceduler.model.ScheduleLog;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServiceTest {
    @Autowired
    private MailService mailService;

    @Test
    public void sendEmail() {
        Schedule schedule = new Schedule();
        schedule.setPerson("DIma");
        schedule.setDescription("Description");
        schedule.setName("name");

        mailService.sendNotificationEmail(schedule);
    }

    @Test
    public void test() throws IOException {
        String s = "{\n" +
                "  \"id\": 10,\n" +
             /*   "  \"created\": \"1906-06-10T20:50: 00.000Z\",\n" +*/
                "  \"schedule\": {\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"test\",\n" +
                "    \"cron\": \"0 */10 * ? * *\",\n" +
                "    \"cronReminder\": null,\n" +
                "    \"periodTxt\": null,\n" +
                "    \"email\": \"berkbach@gmail.com\",\n" +
                "    \"description\": \"Test body description\",\n" +
                "    \"author\": null,\n" +
                "    \"person\": \"Dima\",\n" +
                "    \"start\": \"2018-02-12T11: 35: 15.773+02: 00\",\n" +
                "    \"next\": \"2018-02-13T10: 20: 00+02: 00\",\n" +
                "    \"stop\": null,\n" +
                "    \"createdDate\": null,\n" +
                "    \"active\": false\n" +
                "  },\n" +
                "  \"completed\": true\n" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        ScheduleLog scheduleLog = mapper.readValue(s, ScheduleLog.class);
        System.out.println(scheduleLog);//
    }
}
