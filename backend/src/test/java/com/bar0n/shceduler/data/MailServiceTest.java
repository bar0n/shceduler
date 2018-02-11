package com.bar0n.shceduler.data;

import com.bar0n.shceduler.model.Schedule;
import com.bar0n.shceduler.services.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
}
