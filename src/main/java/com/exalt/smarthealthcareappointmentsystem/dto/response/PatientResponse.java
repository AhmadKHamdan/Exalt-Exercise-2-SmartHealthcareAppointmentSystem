package com.exalt.smarthealthcareappointmentsystem.dto.response;

import java.time.LocalDate;

public record PatientResponse(Long id, String email, String fullName, LocalDate dateOfBirth) {

}