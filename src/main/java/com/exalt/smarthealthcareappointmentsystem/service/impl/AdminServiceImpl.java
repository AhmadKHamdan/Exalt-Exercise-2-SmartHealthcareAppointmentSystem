package com.exalt.smarthealthcareappointmentsystem.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.exalt.smarthealthcareappointmentsystem.dto.request.CreateDoctorRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.request.CreatePatientRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.DoctorResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.PatientResponse;
import com.exalt.smarthealthcareappointmentsystem.entity.user.Doctor;
import com.exalt.smarthealthcareappointmentsystem.entity.user.Patient;
import com.exalt.smarthealthcareappointmentsystem.exception.DoctorNotFoundException;
import com.exalt.smarthealthcareappointmentsystem.exception.DuplicateEmailException;
import com.exalt.smarthealthcareappointmentsystem.mapper.DoctorMapper;
import com.exalt.smarthealthcareappointmentsystem.mapper.PatientMapper;
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
    private final DoctorMapper doctorMapper;
    private final PatientMapper patientMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public DoctorResponse createDoctor(CreateDoctorRequest request) {
        userRepository.findByEmail(request.email())
                .ifPresent(user -> {
                    throw new DuplicateEmailException("An account with this email already exists.");
                });

        if (request.availabilityFrom().isAfter(request.availabilityTill())) {
            throw new IllegalArgumentException("Availability interval is invalid.");
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        Doctor doctor = doctorMapper.toDoctor(request, encodedPassword);
        Doctor savedDoctor = doctorRepository.save(doctor);
        return doctorMapper.toDoctorResponse(savedDoctor);
    }

    @Override
    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll().stream().map(doc -> doctorMapper.toDoctorResponse(doc)).toList();
    }

    @Override
    public void deleteDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with id: " + id));

        doctorRepository.delete(doctor);
    }

    @Override
    public PatientResponse createPatient(CreatePatientRequest request) {
        userRepository.findByEmail(request.email())
                .ifPresent(user -> {
                    throw new DuplicateEmailException("An account with this email already exists.");
                });

        if (request.dateOfBirth().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth is invalid.");
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        Patient patient = patientMapper.toPatient(request, encodedPassword);
        Patient savedPatient = patientRepository.save(patient);
        return patientMapper.toPatientResponse(savedPatient);
    }
}
