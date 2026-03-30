package com.exalt.smarthealthcareappointmentsystem.dto.request.record;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateRecordRequest(@NotNull Long patientId, @NotNull Long appointmentId, @NotBlank String diagnosis,
        List<@NotBlank String> prescriptions, List<@NotBlank String> labResults) {

}
