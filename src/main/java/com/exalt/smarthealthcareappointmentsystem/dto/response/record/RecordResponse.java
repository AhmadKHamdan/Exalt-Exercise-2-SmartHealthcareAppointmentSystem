package com.exalt.smarthealthcareappointmentsystem.dto.response.record;

import java.time.LocalDateTime;
import java.util.List;

import com.exalt.smarthealthcareappointmentsystem.dto.response.appointment.AppointmentSummaryResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.patient.PatientSummaryResponse;

public record RecordResponse(PatientSummaryResponse patient, AppointmentSummaryResponse appointment, String diagnosis,
        List<String> prescriptions, List<String> labResults, LocalDateTime prescriptionDate) {

}
