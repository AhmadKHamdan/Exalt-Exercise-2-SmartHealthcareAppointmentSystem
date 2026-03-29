package com.exalt.smarthealthcareappointmentsystem.service;

import com.exalt.smarthealthcareappointmentsystem.dto.request.appointment.CreateAppointmentRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.appointment.AppointmentResponse;

public interface AppointmentService {

    AppointmentResponse bookAppointment(CreateAppointmentRequest request);

    void cancelAppointment(Long id);
}
