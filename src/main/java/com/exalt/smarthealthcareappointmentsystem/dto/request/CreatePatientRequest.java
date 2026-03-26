package com.exalt.smarthealthcareappointmentsystem.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePatientRequest(@NotBlank String email, @NotBlank String fullName, @NotBlank String password,
        @NotNull LocalDate dateOfBirth) {

}
