package com.exalt.smarthealthcareappointmentsystem.mapper;

import org.springframework.stereotype.Component;

import com.exalt.smarthealthcareappointmentsystem.dto.request.patient.CreatePatientRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.patient.MyPatientProfileResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.patient.PatientResponse;
import com.exalt.smarthealthcareappointmentsystem.entity.user.Patient;

@Component
public class PatientMapper {

    public PatientResponse toPatientResponse(Patient patient) {
        return new PatientResponse(
                patient.getId(),
                patient.getEmail(),
                patient.getFullName(),
                patient.getDateOfBirth());
    }

    public Patient toPatientEntity(CreatePatientRequest request, String encodedPassword) {
        return Patient.builder()
                .email(request.email())
                .fullName(request.fullName())
                .password(encodedPassword)
                .dateOfBirth(request.dateOfBirth())
                .build();
    }

    public MyPatientProfileResponse toMyPatientProfileResponse(Patient patient) {
        return new MyPatientProfileResponse(
                patient.getEmail(),
                patient.getFullName(),
                patient.getDateOfBirth());
    }
}
