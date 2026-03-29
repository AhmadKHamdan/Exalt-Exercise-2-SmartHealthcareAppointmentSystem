package com.exalt.smarthealthcareappointmentsystem.dto.response.appointment;

import java.time.LocalDateTime;

import com.exalt.smarthealthcareappointmentsystem.dto.response.doctor.DoctorSummaryResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.patient.PatientSummaryResponse;
import com.exalt.smarthealthcareappointmentsystem.enums.AppointmentStatus;

public record AppointmentResponse(Long id, LocalDateTime appointmentTime, AppointmentStatus status,
        DoctorSummaryResponse doctor, PatientSummaryResponse patient) {

}
