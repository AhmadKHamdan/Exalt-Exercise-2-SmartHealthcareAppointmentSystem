package com.exalt.smarthealthcareappointmentsystem.dto.request.appointment;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record CreateAppointmentRequest(@NotNull Long doctorId, @NotNull LocalDateTime appointmentTime) {

}
