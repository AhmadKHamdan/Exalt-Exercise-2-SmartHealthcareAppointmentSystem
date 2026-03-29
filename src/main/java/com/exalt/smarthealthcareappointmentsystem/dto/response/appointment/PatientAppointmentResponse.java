package com.exalt.smarthealthcareappointmentsystem.dto.response.appointment;

import java.time.LocalDateTime;

import com.exalt.smarthealthcareappointmentsystem.dto.response.doctor.DoctorSummaryResponse;

public record PatientAppointmentResponse(Long id, LocalDateTime appointmentTime, DoctorSummaryResponse doctor) {

}
