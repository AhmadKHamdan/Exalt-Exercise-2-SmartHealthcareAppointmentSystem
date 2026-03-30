package com.exalt.smarthealthcareappointmentsystem.service;

import java.util.List;

import com.exalt.smarthealthcareappointmentsystem.dto.request.appointment.CreateAppointmentRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.appointment.AppointmentResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.appointment.DoctorAppointmentResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.appointment.PatientAppointmentResponse;

public interface AppointmentService {

    List<AppointmentResponse> getAllAppointments();

    List<PatientAppointmentResponse> getAppointmentsForCurrentPatient();

    List<DoctorAppointmentResponse> getAppointmentsForCurrentDoctor();

    AppointmentResponse bookAppointment(CreateAppointmentRequest request);

    void deleteAppointmentById(Long id);

    void markAppointmentAsCompleted(Long id);
}
