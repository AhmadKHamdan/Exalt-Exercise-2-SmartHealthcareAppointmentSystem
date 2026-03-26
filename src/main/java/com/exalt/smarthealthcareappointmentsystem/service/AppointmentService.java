package com.exalt.smarthealthcareappointmentsystem.service;

import com.exalt.smarthealthcareappointmentsystem.dto.request.CreateAppointmentRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.AppointmentResponse;

public interface AppointmentService {

    AppointmentResponse bookAppointment(CreateAppointmentRequest request);

    void cancelAppointment(Long id);
}
