package com.exalt.smarthealthcareappointmentsystem.dto.request.patient;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePatientRequest(@NotBlank @Email String email, @NotBlank String fullName, @NotBlank String password,
        @NotNull LocalDate dateOfBirth) {

}
