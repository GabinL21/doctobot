package com.doctobot.notifier.service;


import com.doctobot.common.message.AppointmentMessage;
import com.doctobot.common.model.Appointment;
import com.doctobot.notifier.notifier.Notifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class AppointmentConsumer {

    private final MessageConverter messageConverter;
    private final Notifier notifier;

    @RabbitListener(queues = "doctobot.appointments")
    private void processAppointment(Message message) {
        log.info("Received message {}", message);
        AppointmentMessage appointmentMessage = (AppointmentMessage) messageConverter.fromMessage(message);
        List<Appointment> appointments = appointmentMessage.getAppointments();
        List<String> emails = appointmentMessage.getUser().emails();
        notifier.notify(appointments, emails);
    }
}
