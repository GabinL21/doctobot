package com.doctobot.scraper.service;


import com.doctobot.common.message.AppointmentMessage;
import com.doctobot.common.message.LinkMessage;
import com.doctobot.common.model.Appointment;
import com.doctobot.common.model.Link;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class LinkConsumer {

    public static final Duration SLEEP_AFTER_NOTIFICATION = Duration.ofMinutes(4);

    private final Scraper scraper;
    private final MessageConverter messageConverter;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "doctobot.links")
    private void processLink(Message message) throws InterruptedException {
        log.info("Received message {}", message);
        LinkMessage linkMessage = (LinkMessage) messageConverter.fromMessage(message);
        List<String> urls = linkMessage.getLinks().stream().map(Link::url).toList();
        List<Appointment> availableAppointments = getAvailableAppointments(urls);
        if (availableAppointments.isEmpty()) {
            log.info("No availability found");
        } else {
            AppointmentMessage appointmentMessage = new AppointmentMessage(linkMessage.getUser(), availableAppointments);
            sendAppointmentMessage(appointmentMessage);
            Thread.sleep(SLEEP_AFTER_NOTIFICATION.toMillis());
        }
    }

    private List<Appointment> getAvailableAppointments(List<String> urls) {
        List<Appointment> availableAppointments = new ArrayList<>();
        try {
            for (String url : urls) {
                scraper.getPage(url);
                if (scraper.isAppointmentAvailable()) {
                    Appointment appointment = scraper.getAppointment();
                    log.info("Availability found: {}", appointment);
                    availableAppointments.add(appointment);
                }
            }
            return availableAppointments;
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            return availableAppointments;
        } catch (Exception e) {
            log.error(e);
            return availableAppointments;
        } finally {
            scraper.quitWebDriver();
        }
    }

    private void sendAppointmentMessage(AppointmentMessage appointmentMessage) {
        Message message = messageConverter.toMessage(appointmentMessage, new MessageProperties());
        rabbitTemplate.convertAndSend("doctobot.appointments", message);
        log.info("Sent message {}", message);
    }
}
