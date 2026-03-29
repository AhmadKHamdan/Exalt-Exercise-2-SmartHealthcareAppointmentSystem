package com.exalt.smarthealthcareappointmentsystem.dto.request.doctor;

import java.time.LocalTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateDoctorRequest(@NotBlank @Email String email, @NotBlank String fullName, @NotBlank String password,
                @NotBlank String specialty, @NotNull LocalTime availabilityFrom, @NotNull LocalTime availabilityTill) {

}
