package com.exalt.smarthealthcareappointmentsystem.service.impl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.exalt.smarthealthcareappointmentsystem.dto.request.appointment.CreateAppointmentRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.appointment.AppointmentResponse;
import com.exalt.smarthealthcareappointmentsystem.entity.appointment.Appointment;
import com.exalt.smarthealthcareappointmentsystem.entity.user.Doctor;
import com.exalt.smarthealthcareappointmentsystem.entity.user.Patient;
import com.exalt.smarthealthcareappointmentsystem.enums.AppointmentStatus;
import com.exalt.smarthealthcareappointmentsystem.exception.AppointmentConflictException;
import com.exalt.smarthealthcareappointmentsystem.exception.InvalidRequestException;
import com.exalt.smarthealthcareappointmentsystem.exception.ResourceNotFoundException;
import com.exalt.smarthealthcareappointmentsystem.mapper.AppointmentMapper;
import com.exalt.smarthealthcareappointmentsystem.repository.AppointmentRepository;
import com.exalt.smarthealthcareappointmentsystem.repository.DoctorRepository;
import com.exalt.smarthealthcareappointmentsystem.repository.PatientRepository;
import com.exalt.smarthealthcareappointmentsystem.service.AppointmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;

    @Override
    public AppointmentResponse bookAppointment(CreateAppointmentRequest request) {
        Doctor doctor = doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.doctorId()));

        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.patientId()));

        if (request.appointmentTime().isBefore(LocalDateTime.now())) {
            throw new InvalidRequestException("Appointment time cannot be in the past.");
        }

        List<Appointment> bookedAppointmentsForDoctor = appointmentRepository.findByDoctorId(doctor.getId());

        LocalDateTime requestedEnd = request.appointmentTime().plusMinutes(30);
        LocalTime requestedTime = request.appointmentTime().toLocalTime();
        LocalTime requestedEndTime = requestedTime.plusMinutes(30);

        if (requestedTime.isBefore(doctor.getAvailabilityFrom())
                || requestedEndTime.isAfter(doctor.getAvailabilityTill())) {
            throw new AppointmentConflictException("Appointment time is outside doctor's availability.");
        }

        for (var appointment : bookedAppointmentsForDoctor) {
            if (appointment.getStatus() == AppointmentStatus.CANCELED) {
                continue;
            }

            LocalDateTime existingEnd = appointment.getAppointmentTime().plusMinutes(30);

            if (request.appointmentTime().isBefore(existingEnd)
                    && requestedEnd.isAfter(appointment.getAppointmentTime())) {
                throw new AppointmentConflictException("Doctor already has an appointment at this time");
            }
        }

        Appointment appointment = appointmentMapper.toAppointment(request.appointmentTime(), doctor, patient);
        appointment.setStatus(AppointmentStatus.BOOKED);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toAppointmentResponse(savedAppointment);
    }

    @Override
    public void deleteAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

        appointment.setStatus(AppointmentStatus.CANCELED);
        appointmentRepository.save(appointment);
    }
}
