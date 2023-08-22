package com.doctobot.notifier.notifier.email;

import com.doctobot.common.model.Appointment;
import com.doctobot.notifier.notifier.Notifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty(name = "doctobot.notifier.mode", havingValue = "email")
@Log4j2
@RequiredArgsConstructor
public class EmailNotifier implements Notifier {

    private final EmailSender emailSender;

    @Override
    public void notify(List<Appointment> appointments, List<String> emails) {
        String subject = getSubject(appointments);
        String text = getEmailContent(appointments);
        emailSender.sendEmails(subject, text, emails);
        log.info("Email sent for {} appointment(s)", appointments.size());
    }

    private String getSubject(List<Appointment> appointments) {
        return "At least " + appointments.size() + " appointment(s) available!";
    }

    private String getEmailContent(List<Appointment> appointments) {
        StringBuilder content = new StringBuilder(h1("First Available Appointments") + hr());
        for (Appointment appointment : appointments) {
            content.append(getAppointmentContent(appointment));
        }
        return content.toString();
    }

    private String getAppointmentContent(Appointment appointment) {
        return h2(appointment.name()) + a(appointment.date() + " - " + appointment.time(), appointment.url());
    }

    private String h1(String text) {
        return "<h1>" + text + "</h1>";
    }

    private String h2(String text) {
        return "<h2>" + text + "</h2>";
    }

    private String a(String text, String url) {
        return "<a href='" + url + "'>" + text + "</a>";
    }

    private String hr() {
        return "<hr/>";
    }
}
