package com.exalt.smarthealthcareappointmentsystem.dto.response;

import java.time.LocalDateTime;

import com.exalt.smarthealthcareappointmentsystem.enums.AppointmentStatus;

public record AppointmentResponse(Long id, LocalDateTime appointmentTime, AppointmentStatus status,
        DoctorSummaryResponse doctor, PatientSummaryResponse patient) {

}
