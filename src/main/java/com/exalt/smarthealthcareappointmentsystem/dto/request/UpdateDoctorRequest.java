package com.exalt.smarthealthcareappointmentsystem.dto.request;

import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateDoctorRequest(@NotBlank String fullName, @NotBlank String specialty,
                @NotNull LocalTime availabilityFrom, @NotNull LocalTime availabilityTill) {
}