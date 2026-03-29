package com.exalt.smarthealthcareappointmentsystem.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.exalt.smarthealthcareappointmentsystem.dto.request.doctor.CreateDoctorRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.request.doctor.UpdateDoctorRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.request.patient.CreatePatientRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.request.patient.UpdatePatientRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.appointment.AppointmentResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.doctor.DoctorResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.patient.PatientResponse;
import com.exalt.smarthealthcareappointmentsystem.entity.user.Doctor;
import com.exalt.smarthealthcareappointmentsystem.entity.user.Patient;
import com.exalt.smarthealthcareappointmentsystem.enums.Role;
import com.exalt.smarthealthcareappointmentsystem.exception.DuplicateEmailException;
import com.exalt.smarthealthcareappointmentsystem.exception.InvalidRequestException;
import com.exalt.smarthealthcareappointmentsystem.exception.ResourceNotFoundException;
import com.exalt.smarthealthcareappointmentsystem.mapper.AppointmentMapper;
import com.exalt.smarthealthcareappointmentsystem.mapper.DoctorMapper;
import com.exalt.smarthealthcareappointmentsystem.mapper.PatientMapper;
import com.exalt.smarthealthcareappointmentsystem.repository.AppointmentRepository;
import com.exalt.smarthealthcareappointmentsystem.repository.DoctorRepository;
import com.exalt.smarthealthcareappointmentsystem.repository.PatientRepository;
import com.exalt.smarthealthcareappointmentsystem.repository.UserRepository;
import com.exalt.smarthealthcareappointmentsystem.service.AdminService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorMapper doctorMapper;
    private final PatientMapper patientMapper;
    private final AppointmentMapper appointmentMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public DoctorResponse createDoctor(CreateDoctorRequest request) {
        userRepository.findByEmail(request.email())
                .ifPresent(user -> {
                    throw new DuplicateEmailException("An account with this email already exists.");
                });

        if (request.availabilityFrom().isAfter(request.availabilityTill())) {
            throw new InvalidRequestException("Availability interval is invalid.");
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        Doctor doctor = doctorMapper.toDoctorEntity(request, encodedPassword);
        doctor.setRole(Role.ROLE_DOCTOR);
        Doctor savedDoctor = doctorRepository.save(doctor);
        return doctorMapper.toDoctorResponse(savedDoctor);
    }

    @Override
    public void deleteDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));

        doctorRepository.delete(doctor);
    }

    @Override
    public DoctorResponse updateDoctorById(UpdateDoctorRequest request, Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));

        if (request.availabilityFrom().isAfter(request.availabilityTill())
                || request.availabilityFrom().equals(request.availabilityTill())) {
            throw new InvalidRequestException("Availability interval is invalid.");
        }

        doctor.setFullName(request.fullName());
        doctor.setSpecialty(request.specialty());
        doctor.setAvailabilityFrom(request.availabilityFrom());
        doctor.setAvailabilityTill(request.availabilityTill());

        Doctor updatedDoctor = doctorRepository.save(doctor);
        return doctorMapper.toDoctorResponse(updatedDoctor);
    }

    @Override
    public PatientResponse createPatient(CreatePatientRequest request) {
        userRepository.findByEmail(request.email())
                .ifPresent(user -> {
                    throw new DuplicateEmailException("An account with this email already exists.");
                });

        if (request.dateOfBirth().isAfter(LocalDate.now())) {
            throw new InvalidRequestException("Date of birth is invalid.");
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        Patient patient = patientMapper.toPatientEntity(request, encodedPassword);
        patient.setRole(Role.ROLE_PATIENT);
        Patient savedPatient = patientRepository.save(patient);
        return patientMapper.toPatientResponse(savedPatient);
    }

    @Override
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll().stream().map(patientMapper::toPatientResponse).toList();
    }

    @Override
    public void deletePatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

        patientRepository.delete(patient);
    }

    @Override
    public PatientResponse updatePatientById(UpdatePatientRequest request, Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

        if (request.dateOfBirth().isAfter(LocalDate.now())) {
            throw new InvalidRequestException("Date of birth is invalid.");
        }

        patient.setFullName(request.fullName());
        patient.setDateOfBirth(request.dateOfBirth());

        Patient updatedPatient = patientRepository.save(patient);
        return patientMapper.toPatientResponse(updatedPatient);
    }

    @Override
    public List<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAll().stream().map(appointmentMapper::toAppointmentResponse).toList();
    }
}
