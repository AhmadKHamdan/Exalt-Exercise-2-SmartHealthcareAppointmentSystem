package com.exalt.smarthealthcareappointmentsystem.dto.response.record;

import java.time.LocalDateTime;
import java.util.List;

public record RecordSummaryResponse(String id, Long doctorId, Long appointmentId, String diagnosis,
        List<String> prescriptions, List<String> labResults, LocalDateTime prescriptionDate) {

}
