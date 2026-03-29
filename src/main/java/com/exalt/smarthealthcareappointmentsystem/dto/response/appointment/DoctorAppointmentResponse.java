package com.exalt.smarthealthcareappointmentsystem.dto.response.appointment;

import java.time.LocalDateTime;

import com.exalt.smarthealthcareappointmentsystem.dto.response.patient.PatientResponse;

public record DoctorAppointmentResponse(Long id, LocalDateTime appointmentTime, PatientResponse patient) {

}
