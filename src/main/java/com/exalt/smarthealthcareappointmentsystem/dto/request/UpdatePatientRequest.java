package com.exalt.smarthealthcareappointmentsystem.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdatePatientRequest(@NotBlank String fullName, @NotNull LocalDate dateOfBirth) {

}
