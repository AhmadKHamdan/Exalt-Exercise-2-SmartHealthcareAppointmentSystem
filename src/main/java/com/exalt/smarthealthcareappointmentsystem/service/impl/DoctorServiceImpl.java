package com.exalt.smarthealthcareappointmentsystem.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.exalt.smarthealthcareappointmentsystem.dto.request.doctor.CreateDoctorRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.request.doctor.UpdateDoctorRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.doctor.DoctorResponse;
import com.exalt.smarthealthcareappointmentsystem.entity.user.Doctor;
import com.exalt.smarthealthcareappointmentsystem.enums.Role;
import com.exalt.smarthealthcareappointmentsystem.exception.DuplicateEmailException;
import com.exalt.smarthealthcareappointmentsystem.exception.InvalidRequestException;
import com.exalt.smarthealthcareappointmentsystem.exception.ResourceNotFoundException;
import com.exalt.smarthealthcareappointmentsystem.mapper.DoctorMapper;
import com.exalt.smarthealthcareappointmentsystem.repository.DoctorRepository;
import com.exalt.smarthealthcareappointmentsystem.repository.UserRepository;
import com.exalt.smarthealthcareappointmentsystem.service.DoctorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<DoctorResponse> getDoctors(String specialty) {
        List<Doctor> doctors;

        if (specialty != null && !specialty.isBlank()) {
            doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        } else {
            doctors = doctorRepository.findAll();
        }

        return doctors.stream().map(doctorMapper::toDoctorResponse).toList();
    }

    @Override
    public DoctorResponse getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));

        return doctorMapper.toDoctorResponse(doctor);
    }

    @Override
    public DoctorResponse createDoctor(CreateDoctorRequest request) {
        userRepository.findByEmail(request.email())
                .ifPresent(user -> {
                    throw new DuplicateEmailException("An account with this email already exists.");
                });

        if (!request.availabilityFrom().isBefore(request.availabilityTill())) {
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
}
