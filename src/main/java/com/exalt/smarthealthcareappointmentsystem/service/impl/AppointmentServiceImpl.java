package com.exalt.smarthealthcareappointmentsystem.service.impl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.exalt.smarthealthcareappointmentsystem.audit.LogAction;
import com.exalt.smarthealthcareappointmentsystem.dto.request.appointment.CreateAppointmentRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.appointment.AppointmentResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.appointment.DoctorAppointmentResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.appointment.PatientAppointmentResponse;
import com.exalt.smarthealthcareappointmentsystem.entity.appointment.Appointment;
import com.exalt.smarthealthcareappointmentsystem.entity.user.Doctor;
import com.exalt.smarthealthcareappointmentsystem.entity.user.Patient;
import com.exalt.smarthealthcareappointmentsystem.enums.AppointmentStatus;
import com.exalt.smarthealthcareappointmentsystem.exception.AccessDeniedException;
import com.exalt.smarthealthcareappointmentsystem.exception.AppointmentConflictException;
import com.exalt.smarthealthcareappointmentsystem.exception.InvalidRequestException;
import com.exalt.smarthealthcareappointmentsystem.exception.ResourceNotFoundException;
import com.exalt.smarthealthcareappointmentsystem.mapper.AppointmentMapper;
import com.exalt.smarthealthcareappointmentsystem.repository.AppointmentRepository;
import com.exalt.smarthealthcareappointmentsystem.repository.DoctorRepository;
import com.exalt.smarthealthcareappointmentsystem.service.AppointmentService;
import com.exalt.smarthealthcareappointmentsystem.util.AuthenticationUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final AuthenticationUtils authenticationUtils;

    @Override
    public List<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAll().stream().map(appointmentMapper::toAppointmentResponse).toList();
    }

    @Override
    @LogAction("Appointment booked")
    public AppointmentResponse bookAppointment(CreateAppointmentRequest request) {
        Patient patient = authenticationUtils.getAuthenticatedPatient();
        Doctor doctor = doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.doctorId()));

        if (request.appointmentTime().isBefore(LocalDateTime.now())) {
            throw new InvalidRequestException("Appointment time cannot be in the past.");
        }

        List<Appointment> bookedAppointmentsForDoctor = appointmentRepository.findByDoctorIdAndStatus(doctor.getId(),
                AppointmentStatus.BOOKED);

        LocalDateTime requestedEnd = request.appointmentTime().plusMinutes(30);
        LocalTime requestedEndTime = requestedEnd.toLocalTime();
        LocalTime requestedTime = request.appointmentTime().toLocalTime();

        if (requestedTime.isBefore(doctor.getAvailabilityFrom())
                || requestedEndTime.isAfter(doctor.getAvailabilityTill())) {
            throw new AppointmentConflictException("Appointment time is outside doctor's availability.");
        }

        for (var appointment : bookedAppointmentsForDoctor) {
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
    @LogAction("Appointment canceled")
    public void cancelAppointmentById(Long id) {
        Patient patient = authenticationUtils.getAuthenticatedPatient();

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

        if (!appointment.getPatient().getId().equals(patient.getId())) {
            throw new AccessDeniedException("You are not allowed to cancel this appointment.");
        }

        if (appointment.getStatus() == AppointmentStatus.CANCELED) {
            throw new InvalidRequestException("Appointment is already canceled.");
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new InvalidRequestException("Completed appointments cannot be canceled.");
        }

        appointment.setStatus(AppointmentStatus.CANCELED);
        appointmentRepository.save(appointment);
    }

    @Override
    public void markAppointmentAsCompleted(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);
    }

    @Override
    public List<PatientAppointmentResponse> getAppointmentsForCurrentPatient() {
        Patient patient = authenticationUtils.getAuthenticatedPatient();
        List<Appointment> appointments = appointmentRepository.findByPatientIdAndStatus(patient.getId(),
                AppointmentStatus.BOOKED);

        return appointments.stream().map(appointmentMapper::toPatientAppointmentResponse).toList();
    }

    @Override
    public List<DoctorAppointmentResponse> getAppointmentsForCurrentDoctor() {
        Doctor doctor = authenticationUtils.getAuthenticatedDoctor();
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndStatus(doctor.getId(),
                AppointmentStatus.BOOKED);

        return appointments.stream().map(appointmentMapper::toDoctorAppointmentResponse).toList();
    }
}
