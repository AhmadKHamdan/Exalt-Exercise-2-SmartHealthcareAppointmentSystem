package com.exalt.smarthealthcareappointmentsystem.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.exalt.smarthealthcareappointmentsystem.dto.response.AppointmentResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.DoctorSummaryResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.PatientSummaryResponse;
import com.exalt.smarthealthcareappointmentsystem.entity.appointment.Appointment;
import com.exalt.smarthealthcareappointmentsystem.entity.user.Doctor;
import com.exalt.smarthealthcareappointmentsystem.entity.user.Patient;

@Component
public class AppointmentMapper {

    public AppointmentResponse toAppointmentResponse(Appointment appointment) {
        Doctor doctor = appointment.getDoctor();
        Patient patient = appointment.getPatient();
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getAppointmentTime(),
                appointment.getStatus(),
                new DoctorSummaryResponse(doctor.getId(), doctor.getFullName(), doctor.getSpecialty()),
                new PatientSummaryResponse(patient.getId(), patient.getFullName()));
    }

    public Appointment toAppointment(LocalDateTime appointmentTime, Doctor doctor, Patient patient) {
        return Appointment.builder()
                .appointmentTime(appointmentTime)
                .patient(patient)
                .doctor(doctor)
                .build();
    }
}