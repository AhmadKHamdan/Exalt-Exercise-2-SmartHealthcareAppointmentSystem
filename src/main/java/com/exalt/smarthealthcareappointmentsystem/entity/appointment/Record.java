package com.exalt.smarthealthcareappointmentsystem.entity.appointment;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "records")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Record {

    @Id
    private String id;

    @Indexed
    private Long patientId;

    @Indexed
    private Long doctorId;

    @Indexed
    private Long appointmentId;

    @NotBlank
    private String diagnosis;

    private List<String> prescriptions;

    private List<String> labResults;

    private LocalDateTime prescriptionDate;
}
