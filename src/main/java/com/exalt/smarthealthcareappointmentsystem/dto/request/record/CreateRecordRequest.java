package com.exalt.smarthealthcareappointmentsystem.dto.request.record;

import java.util.List;

public record CreateRecordRequest(Long patientId, Long appointmentId, String diagnosis,
        List<String> prescriptions, List<String> labResults) {

}
