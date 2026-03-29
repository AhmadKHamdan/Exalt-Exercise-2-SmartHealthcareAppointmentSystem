package com.exalt.smarthealthcareappointmentsystem.mapper;

import org.springframework.stereotype.Component;

import com.exalt.smarthealthcareappointmentsystem.dto.request.doctor.CreateDoctorRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.doctor.DoctorResponse;
import com.exalt.smarthealthcareappointmentsystem.entity.user.Doctor;

@Component
public class DoctorMapper {

    public DoctorResponse toDoctorResponse(Doctor doctor) {
        return new DoctorResponse(
                doctor.getId(),
                doctor.getEmail(),
                doctor.getFullName(),
                doctor.getSpecialty(),
                doctor.getAvailabilityFrom(),
                doctor.getAvailabilityTill());
    }

    public Doctor toDoctor(CreateDoctorRequest request, String encodedPassword) {
        return Doctor.builder()
                .email(request.email())
                .fullName(request.fullName())
                .password(encodedPassword)
                .specialty(request.specialty())
                .availabilityFrom(request.availabilityFrom())
                .availabilityTill(request.availabilityTill())
                .build();
    }
}