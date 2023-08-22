package com.doctobot.notifier.notifier;

import com.doctobot.common.model.Appointment;

import java.util.List;

public interface Notifier {

    void notify(List<Appointment> appointments, List<String> emails);
}
