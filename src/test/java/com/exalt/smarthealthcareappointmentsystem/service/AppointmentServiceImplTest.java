package com.exalt.smarthealthcareappointmentsystem.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exalt.smarthealthcareappointmentsystem.dto.request.appointment.CreateAppointmentRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.appointment.AppointmentResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.doctor.DoctorSummaryResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.patient.PatientSummaryResponse;
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
import com.exalt.smarthealthcareappointmentsystem.service.impl.AppointmentServiceImpl;
import com.exalt.smarthealthcareappointmentsystem.util.AuthenticationUtils;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentMapper appointmentMapper;

    @Mock
    private AuthenticationUtils authenticationUtils;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private Doctor doctor;
    private Patient patient;

    private static final LocalDateTime APPOINTMENT_TIME = LocalDateTime.now().plusDays(1);

    @BeforeEach
    void setUp() {
        doctor = Doctor.builder()
                .id(1L)
                .email("drahmad@gmail.com")
                .fullName("Dr. Ahmad")
                .specialty("Cardiology")
                .availabilityFrom(LocalTime.of(9, 0))
                .availabilityTill(LocalTime.of(17, 0))
                .build();

        patient = Patient.builder()
                .id(2L)
                .email("ahmad@gmail.com")
                .fullName("Ahmad")
                .build();
    }

    @Test
    @DisplayName("Should successfully book appointment when all conditions are met")
    void bookAppointment_validRequest_returnsAppointmentResponse() {
        CreateAppointmentRequest request = new CreateAppointmentRequest(doctor.getId(), APPOINTMENT_TIME);

        Appointment savedAppointment = Appointment.builder()
                .id(10L)
                .doctor(doctor)
                .patient(patient)
                .appointmentTime(APPOINTMENT_TIME)
                .status(AppointmentStatus.BOOKED)
                .build();

        AppointmentResponse expectedResponse = new AppointmentResponse(10L, APPOINTMENT_TIME, AppointmentStatus.BOOKED,
                new DoctorSummaryResponse(doctor.getId(), doctor.getFullName(), doctor.getSpecialty()),
                new PatientSummaryResponse(patient.getId(), patient.getFullName()));

        when(authenticationUtils.getAuthenticatedPatient()).thenReturn(patient);
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findByDoctorIdAndStatus(doctor.getId(), AppointmentStatus.BOOKED))
                .thenReturn(List.of());
        when(appointmentMapper.toAppointment(APPOINTMENT_TIME, doctor, patient)).thenReturn(savedAppointment);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(savedAppointment);
        when(appointmentMapper.toAppointmentResponse(savedAppointment)).thenReturn(expectedResponse);

        AppointmentResponse response = appointmentService.bookAppointment(request);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.status()).isEqualTo(AppointmentStatus.BOOKED);
        assertThat(response.doctor().id()).isEqualTo(doctor.getId());
        assertThat(response.patient().id()).isEqualTo(patient.getId());

        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when doctor does not exist")
    void bookAppointment_doctorNotFound_throwsResourceNotFoundException() {
        CreateAppointmentRequest request = new CreateAppointmentRequest(99L, APPOINTMENT_TIME);

        when(doctorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> appointmentService.bookAppointment(request))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(appointmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw InvalidRequestException when appointment time is in the past")
    void bookAppointment_pastTime_throwsInvalidRequestException() {
        LocalDateTime pastTime = LocalDateTime.now().minusHours(1);
        CreateAppointmentRequest request = new CreateAppointmentRequest(doctor.getId(), pastTime);

        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));

        assertThatThrownBy(() -> appointmentService.bookAppointment(request))
                .isInstanceOf(InvalidRequestException.class);

        verify(appointmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw AppointmentConflictException when appointment starts before doctor availability")
    void bookAppointment_beforeAvailability_throwsAppointmentConflictException() {
        LocalDateTime tooEarly = LocalDateTime.now().plusDays(1).withHour(8).withMinute(0).withSecond(0).withNano(0);
        CreateAppointmentRequest request = new CreateAppointmentRequest(doctor.getId(), tooEarly);

        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findByDoctorIdAndStatus(doctor.getId(), AppointmentStatus.BOOKED))
                .thenReturn(List.of());

        assertThatThrownBy(() -> appointmentService.bookAppointment(request))
                .isInstanceOf(AppointmentConflictException.class);

        verify(appointmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw AppointmentConflictException when new appointment overlaps existing one")
    void bookAppointment_overlapsExistingAppointment_throwsAppointmentConflictException() {
        LocalDateTime existingTime = APPOINTMENT_TIME;
        LocalDateTime overlappingTime = APPOINTMENT_TIME.plusMinutes(15);

        Appointment existingAppointment = Appointment.builder()
                .id(5L)
                .doctor(doctor)
                .patient(patient)
                .appointmentTime(existingTime)
                .status(AppointmentStatus.BOOKED)
                .build();

        CreateAppointmentRequest request = new CreateAppointmentRequest(doctor.getId(), overlappingTime);

        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findByDoctorIdAndStatus(doctor.getId(), AppointmentStatus.BOOKED))
                .thenReturn(List.of(existingAppointment));

        assertThatThrownBy(() -> appointmentService.bookAppointment(request))
                .isInstanceOf(AppointmentConflictException.class);

        verify(appointmentRepository, never()).save(any());
    }
}