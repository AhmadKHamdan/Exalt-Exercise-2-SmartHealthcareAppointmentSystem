package com.exalt.smarthealthcareappointmentsystem.service;

import java.util.List;

import com.exalt.smarthealthcareappointmentsystem.dto.request.appointment.CreateAppointmentRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.appointment.AppointmentResponse;

public interface AppointmentService {

    List<AppointmentResponse> getAllAppointments();

    AppointmentResponse bookAppointment(CreateAppointmentRequest request);

    void deleteAppointmentById(Long id);

    void markAppointmentAsCompleted(Long id);
}
