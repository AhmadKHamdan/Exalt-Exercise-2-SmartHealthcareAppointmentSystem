package com.exalt.smarthealthcareappointmentsystem.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.exalt.smarthealthcareappointmentsystem.dto.request.CreateAppointmentRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.AppointmentResponse;
import com.exalt.smarthealthcareappointmentsystem.entity.appointment.Appointment;
import com.exalt.smarthealthcareappointmentsystem.entity.user.Doctor;
import com.exalt.smarthealthcareappointmentsystem.entity.user.Patient;
import com.exalt.smarthealthcareappointmentsystem.enums.AppointmentStatus;
import com.exalt.smarthealthcareappointmentsystem.exception.AppointmentConflictException;
import com.exalt.smarthealthcareappointmentsystem.exception.UserNotFoundException;
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
                .orElseThrow(() -> new UserNotFoundException("Doctor not found with id: " + request.doctorId()));

        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new UserNotFoundException("Patient not found with id: " + request.patientId()));

        List<Appointment> bookedAppointmentsForDoctor = appointmentRepository.findByDoctorId(doctor.getId());

        for (var appointment : bookedAppointmentsForDoctor) {
            LocalDateTime existingStart = appointment.getAppointmentTime();
            LocalDateTime existingEnd = existingStart.plusMinutes(30);

            LocalDateTime requestedStart = request.appointmentTime();
            LocalDateTime requestedEnd = requestedStart.plusMinutes(30);

            if (!requestedEnd.isBefore(existingStart) && !requestedStart.isAfter(existingEnd)) {
                throw new AppointmentConflictException("Doctor already has an appointment at this time");
            }
        }

        Appointment appointment = appointmentMapper.toAppointment(request.appointmentTime(), doctor, patient);
        appointment.setStatus(AppointmentStatus.BOOKED);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toAppointmentResponse(savedAppointment);
    }

    @Override
    public void cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Appointment not found with id: " + id));

        appointmentRepository.delete(appointment);
    }
}
