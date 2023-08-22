package com.doctobot.common.message;

import com.doctobot.common.model.Appointment;
import com.doctobot.common.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class AppointmentMessage {

    private User user;
    private List<Appointment> appointments;
}
