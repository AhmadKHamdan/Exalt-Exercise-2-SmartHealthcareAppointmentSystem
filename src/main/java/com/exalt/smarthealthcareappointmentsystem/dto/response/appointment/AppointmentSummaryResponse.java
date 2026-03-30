package com.exalt.smarthealthcareappointmentsystem.dto.response.appointment;

import java.time.LocalDateTime;

import com.exalt.smarthealthcareappointmentsystem.enums.AppointmentStatus;

public record AppointmentSummaryResponse(Long id, LocalDateTime appointmentTime, AppointmentStatus status) {

}
