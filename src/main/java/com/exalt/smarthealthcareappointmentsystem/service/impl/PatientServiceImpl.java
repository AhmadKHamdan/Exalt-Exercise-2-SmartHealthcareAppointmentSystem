package com.exalt.smarthealthcareappointmentsystem.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.exalt.smarthealthcareappointmentsystem.dto.request.patient.CreatePatientRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.request.patient.UpdatePatientRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.patient.MyPatientProfileResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.patient.PatientResponse;
import com.exalt.smarthealthcareappointmentsystem.entity.user.Patient;
import com.exalt.smarthealthcareappointmentsystem.enums.Role;
import com.exalt.smarthealthcareappointmentsystem.exception.DuplicateEmailException;
import com.exalt.smarthealthcareappointmentsystem.exception.InvalidRequestException;
import com.exalt.smarthealthcareappointmentsystem.exception.ResourceNotFoundException;
import com.exalt.smarthealthcareappointmentsystem.mapper.PatientMapper;
import com.exalt.smarthealthcareappointmentsystem.repository.PatientRepository;
import com.exalt.smarthealthcareappointmentsystem.repository.UserRepository;
import com.exalt.smarthealthcareappointmentsystem.service.PatientService;
import com.exalt.smarthealthcareappointmentsystem.util.AuthenticationUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationUtils authenticationUtils;

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
    public MyPatientProfileResponse updateMyProfile(UpdatePatientRequest request) {
        Patient patient = authenticationUtils.getAuthenticatedPatient();

        if (request.dateOfBirth().isAfter(LocalDate.now())) {
            throw new InvalidRequestException("Date of birth is invalid.");
        }

        patient.setFullName(request.fullName());
        patient.setDateOfBirth(request.dateOfBirth());

        Patient updatedPatient = patientRepository.save(patient);
        return patientMapper.toMyPatientProfileResponse(updatedPatient);
    }
}
