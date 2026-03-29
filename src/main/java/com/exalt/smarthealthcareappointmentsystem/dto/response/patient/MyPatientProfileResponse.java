package com.exalt.smarthealthcareappointmentsystem.dto.response.patient;

import java.time.LocalDate;

public record MyPatientProfileResponse(String email, String fullName, LocalDate dateOfBirth) {

}
