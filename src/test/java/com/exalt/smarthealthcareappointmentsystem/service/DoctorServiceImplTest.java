package com.exalt.smarthealthcareappointmentsystem.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.exalt.smarthealthcareappointmentsystem.dto.request.doctor.CreateDoctorRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.doctor.DoctorResponse;
import com.exalt.smarthealthcareappointmentsystem.entity.user.Doctor;
import com.exalt.smarthealthcareappointmentsystem.enums.Role;
import com.exalt.smarthealthcareappointmentsystem.exception.DuplicateEmailException;
import com.exalt.smarthealthcareappointmentsystem.exception.InvalidRequestException;
import com.exalt.smarthealthcareappointmentsystem.mapper.DoctorMapper;
import com.exalt.smarthealthcareappointmentsystem.repository.DoctorRepository;
import com.exalt.smarthealthcareappointmentsystem.repository.UserRepository;
import com.exalt.smarthealthcareappointmentsystem.service.impl.DoctorServiceImpl;

@ExtendWith(MockitoExtension.class)
class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorMapper doctorMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    private CreateDoctorRequest validRequest;
    private Doctor doctorEntity;
    private DoctorResponse doctorResponse;

    @BeforeEach
    void setUp() {
        validRequest = new CreateDoctorRequest(
                "drahmad@gmail.com",
                "Dr. Ahmad",
                "Ahmad123",
                "Cardiology",
                LocalTime.of(9, 0),
                LocalTime.of(17, 0));

        doctorEntity = Doctor.builder()
                .id(1L)
                .email(validRequest.email())
                .fullName(validRequest.fullName())
                .password("encodedPassword")
                .specialty(validRequest.specialty())
                .availabilityFrom(LocalTime.of(9, 0))
                .availabilityTill(LocalTime.of(17, 0))
                .role(Role.ROLE_DOCTOR)
                .build();

        doctorResponse = new DoctorResponse(
                1L,
                validRequest.email(),
                validRequest.fullName(),
                validRequest.specialty(),
                LocalTime.of(9, 0),
                LocalTime.of(17, 0));
    }

    @Test
    @DisplayName("Should successfully create a doctor when all inputs are valid")
    void createDoctor_validRequest_returnsDoctorResponse() {
        when(userRepository.findByEmail(validRequest.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(validRequest.password())).thenReturn("encodedPassword");
        when(doctorMapper.toDoctorEntity(validRequest, "encodedPassword")).thenReturn(doctorEntity);
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctorEntity);
        when(doctorMapper.toDoctorResponse(doctorEntity)).thenReturn(doctorResponse);

        DoctorResponse response = doctorService.createDoctor(validRequest);

        assertThat(response).isNotNull();
        assertThat(response.email()).isEqualTo(validRequest.email());
        assertThat(response.specialty()).isEqualTo(validRequest.specialty());

        verify(doctorRepository).save(any(Doctor.class));
    }

    @Test
    @DisplayName("Should set ROLE_DOCTOR before saving")
    void createDoctor_validRequest_setsRoleBeforeSaving() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(doctorMapper.toDoctorEntity(any(), anyString())).thenReturn(doctorEntity);
        when(doctorRepository.save(any(Doctor.class))).thenAnswer(inv -> inv.getArgument(0));
        when(doctorMapper.toDoctorResponse(any())).thenReturn(doctorResponse);

        doctorService.createDoctor(validRequest);

        verify(doctorRepository).save(argThat(doctor -> doctor.getRole() == Role.ROLE_DOCTOR));
    }

    @Test
    @DisplayName("Should throw DuplicateEmailException when email is already registered")
    void createDoctor_duplicateEmail_throwsDuplicateEmailException() {
        when(userRepository.findByEmail(validRequest.email())).thenReturn(Optional.of(doctorEntity));

        assertThatThrownBy(() -> doctorService.createDoctor(validRequest)).isInstanceOf(DuplicateEmailException.class);

        verify(doctorRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw InvalidRequestException when availabilityFrom is after availabilityTill")
    void createDoctor_fromAfterTill_throwsInvalidRequestException() {
        CreateDoctorRequest invalidRequest = new CreateDoctorRequest(
                "drahmad@gmail.com",
                "Dr. Ahmad",
                "Ahmad123",
                "Cardiology",
                LocalTime.of(17, 0),
                LocalTime.of(9, 0));

        when(userRepository.findByEmail(invalidRequest.email())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> doctorService.createDoctor(invalidRequest))
                .isInstanceOf(InvalidRequestException.class);

        verify(doctorRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw InvalidRequestException when availabilityFrom equals availabilityTill")
    void createDoctor_fromEqualsTill_throwsInvalidRequestException() {
        CreateDoctorRequest invalidRequest = new CreateDoctorRequest(
                "drahmad@gmail.com",
                "Dr. Ahmad",
                "Ahmad123",
                "Cardiology",
                LocalTime.of(9, 0),
                LocalTime.of(9, 0));

        when(userRepository.findByEmail(invalidRequest.email())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> doctorService.createDoctor(invalidRequest))
                .isInstanceOf(InvalidRequestException.class);

        verify(doctorRepository, never()).save(any());
    }
}