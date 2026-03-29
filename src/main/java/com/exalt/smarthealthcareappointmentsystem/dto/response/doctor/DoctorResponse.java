package com.exalt.smarthealthcareappointmentsystem.dto.response.doctor;

import java.time.LocalTime;

public record DoctorResponse(Long id, String email, String fullName, String specialty, LocalTime availabilityFrom,
                LocalTime availabilityTill) {

}
