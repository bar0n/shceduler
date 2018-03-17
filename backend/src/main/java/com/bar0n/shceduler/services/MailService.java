package com.bar0n.shceduler.services;

import com.bar0n.shceduler.model.Schedule;
import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.util.Locale;

/**
 * Service for sending emails.
 * <p>
 * We use the @Async annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";


    private final JavaMailSender javaMailSender;


    private final SpringTemplateEngine templateEngine;

    public MailService(JavaMailSender javaMailSender,
                       SpringTemplateEngine templateEngine) {

        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to.split("[\\,,\\;]"));
            message.setFrom("berkbach@gmail.com");
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.warn("Email could not be sent to user '{}'", to, e);
            } else {
                log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
            }
        }
    }

    @Async
    public void sendEmailFromTemplate(Schedule schedule, String templateName, String subject) {
        Locale locale = Locale.forLanguageTag("en");
        Context context = new Context(locale);
        context.setVariable("schedule", schedule);
        String content = templateEngine.process(templateName, context);
        sendEmail(schedule.getEmail(), subject, content, false, true);

    }

    @Async
    public void sendNotificationEmail(Schedule schedule) {
        log.debug("Sending Notification email to '{}'", schedule.getEmail());
        sendEmailFromTemplate(schedule, "notificationEmail", schedule.getName());
    }

    @Async
    public void sendNotificationReminderEmail(Schedule schedule) {
        log.debug("Sending Notification email to '{}'", schedule.getEmail());
        sendEmailFromTemplate(schedule, "notificationEmail", "Reminder " + schedule.getName());
    }

}
