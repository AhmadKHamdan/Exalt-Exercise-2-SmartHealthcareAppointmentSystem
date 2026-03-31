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

import com.exalt.smarthealthcareappointmentsystem.dto.request.record.CreateRecordRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.appointment.AppointmentSummaryResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.patient.PatientSummaryResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.record.RecordResponse;
import com.exalt.smarthealthcareappointmentsystem.entity.appointment.Appointment;
import com.exalt.smarthealthcareappointmentsystem.entity.appointment.Record;
import com.exalt.smarthealthcareappointmentsystem.entity.user.Doctor;
import com.exalt.smarthealthcareappointmentsystem.entity.user.Patient;
import com.exalt.smarthealthcareappointmentsystem.enums.AppointmentStatus;
import com.exalt.smarthealthcareappointmentsystem.exception.AccessDeniedException;
import com.exalt.smarthealthcareappointmentsystem.exception.InvalidRequestException;
import com.exalt.smarthealthcareappointmentsystem.exception.ResourceNotFoundException;
import com.exalt.smarthealthcareappointmentsystem.mapper.RecordMapper;
import com.exalt.smarthealthcareappointmentsystem.repository.AppointmentRepository;
import com.exalt.smarthealthcareappointmentsystem.repository.DoctorRepository;
import com.exalt.smarthealthcareappointmentsystem.repository.PatientRepository;
import com.exalt.smarthealthcareappointmentsystem.repository.RecordRepository;
import com.exalt.smarthealthcareappointmentsystem.service.impl.RecordServiceImpl;
import com.exalt.smarthealthcareappointmentsystem.util.AuthenticationUtils;

@ExtendWith(MockitoExtension.class)
class RecordServiceImplTest {

    @Mock
    private RecordRepository recordRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private RecordMapper recordMapper;
    
    @Mock
    private AuthenticationUtils authenticationUtils;

    @InjectMocks
    private RecordServiceImpl recordService;

    private Doctor doctor;
    private Doctor secondDoctor;
    private Patient patient;
    private Appointment completedAppointment;
    private CreateRecordRequest validRequest;

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

        secondDoctor = Doctor.builder()
                .id(2L)
                .email("drmostafa@gmail.com")
                .fullName("Dr. Mostafa")
                .specialty("Neurology")
                .availabilityFrom(LocalTime.of(9, 0))
                .availabilityTill(LocalTime.of(17, 0))
                .build();

        patient = Patient.builder()
                .id(10L)
                .email("ahmad@gmail.com")
                .fullName("Ahmad")
                .build();

        completedAppointment = Appointment.builder()
                .id(100L)
                .doctor(doctor)
                .patient(patient)
                .appointmentTime(LocalDateTime.now().minusDays(1))
                .status(AppointmentStatus.COMPLETED)
                .build();

        validRequest = new CreateRecordRequest(patient.getId(), completedAppointment.getId(),
                "Cold", List.of(), List.of());
    }

    @Test
    @DisplayName("Should successfully create a record for a completed appointment")
    void createRecord_validRequest_returnsRecordResponse() {
        Record savedRecord = Record.builder()
                .id("rec1")
                .doctorId(doctor.getId())
                .patientId(patient.getId())
                .appointmentId(completedAppointment.getId())
                .diagnosis(validRequest.diagnosis())
                .prescriptions(validRequest.prescriptions())
                .labResults(validRequest.labResults())
                .prescriptionDate(LocalDateTime.now())
                .build();

        RecordResponse expectedResponse = new RecordResponse("rec1",
                new PatientSummaryResponse(patient.getId(), patient.getFullName()),
                new AppointmentSummaryResponse(completedAppointment.getId(), completedAppointment.getAppointmentTime(),
                        AppointmentStatus.COMPLETED),
                validRequest.diagnosis(), validRequest.prescriptions(), validRequest.labResults(),
                savedRecord.getPrescriptionDate());

        when(authenticationUtils.getAuthenticatedDoctor()).thenReturn(doctor);
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(appointmentRepository.findById(completedAppointment.getId()))
                .thenReturn(Optional.of(completedAppointment));
        when(recordMapper.toRecordEntity(validRequest, doctor.getId())).thenReturn(savedRecord);
        when(recordRepository.save(any(Record.class))).thenReturn(savedRecord);
        when(recordMapper.toRecordResponse(savedRecord, patient, completedAppointment)).thenReturn(expectedResponse);

        RecordResponse response = recordService.createRecord(validRequest);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo("rec1");
        assertThat(response.diagnosis()).isEqualTo("Cold");
        verify(recordRepository).save(any(Record.class));
    }

    @Test
    @DisplayName("Should throw AccessDeniedException when doctor tries to create record for another doctor's appointment")
    void createRecord_doctorNotOwnerOfAppointment_throwsAccessDeniedException() {
        Appointment appointmentOfSecondDoctor = Appointment.builder()
                .id(100L)
                .doctor(secondDoctor)
                .patient(patient)
                .appointmentTime(LocalDateTime.now().minusDays(1))
                .status(AppointmentStatus.COMPLETED)
                .build();

        when(authenticationUtils.getAuthenticatedDoctor()).thenReturn(doctor);
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(appointmentRepository.findById(completedAppointment.getId()))
                .thenReturn(Optional.of(appointmentOfSecondDoctor));

        assertThatThrownBy(() -> recordService.createRecord(validRequest)).isInstanceOf(AccessDeniedException.class);

        verify(recordRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw InvalidRequestException when patient in request does not match appointment's patient")
    void createRecord_patientMismatch_throwsInvalidRequestException() {
        Patient secondPatient = Patient.builder()
                .id(99L)
                .email("other@email.com")
                .fullName("Other Patient")
                .build();

        Appointment appointmentWithSecondPatient = Appointment.builder()
                .id(100L)
                .doctor(doctor)
                .patient(secondPatient)
                .appointmentTime(LocalDateTime.now().minusDays(1))
                .status(AppointmentStatus.COMPLETED)
                .build();

        when(authenticationUtils.getAuthenticatedDoctor()).thenReturn(doctor);
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(appointmentRepository.findById(completedAppointment.getId()))
                .thenReturn(Optional.of(appointmentWithSecondPatient));

        assertThatThrownBy(() -> recordService.createRecord(validRequest)).isInstanceOf(InvalidRequestException.class);

        verify(recordRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw InvalidRequestException when appointment is still BOOKED")
    void createRecord_appointmentStillBooked_throwsInvalidRequestException() {
        Appointment bookedAppointment = Appointment.builder()
                .id(100L)
                .doctor(doctor)
                .patient(patient)
                .appointmentTime(LocalDateTime.now().plusDays(1))
                .status(AppointmentStatus.BOOKED)
                .build();

        when(authenticationUtils.getAuthenticatedDoctor()).thenReturn(doctor);
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(appointmentRepository.findById(completedAppointment.getId())).thenReturn(Optional.of(bookedAppointment));

        assertThatThrownBy(() -> recordService.createRecord(validRequest)).isInstanceOf(InvalidRequestException.class);

        verify(recordRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw InvalidRequestException when appointment is CANCELED")
    void createRecord_appointmentCanceled_throwsInvalidRequestException() {
        Appointment canceledAppointment = Appointment.builder()
                .id(100L)
                .doctor(doctor)
                .patient(patient)
                .appointmentTime(LocalDateTime.now().minusDays(1))
                .status(AppointmentStatus.CANCELED)
                .build();

        when(authenticationUtils.getAuthenticatedDoctor()).thenReturn(doctor);
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(appointmentRepository.findById(completedAppointment.getId()))
                .thenReturn(Optional.of(canceledAppointment));

        assertThatThrownBy(() -> recordService.createRecord(validRequest)).isInstanceOf(InvalidRequestException.class);

        verify(recordRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when patient does not exist")
    void createRecord_patientNotFound_throwsResourceNotFoundException() {
        when(authenticationUtils.getAuthenticatedDoctor()).thenReturn(doctor);
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recordService.createRecord(validRequest))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(recordRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when appointment does not exist")
    void createRecord_appointmentNotFound_throwsResourceNotFoundException() {
        when(authenticationUtils.getAuthenticatedDoctor()).thenReturn(doctor);
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(appointmentRepository.findById(completedAppointment.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recordService.createRecord(validRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(completedAppointment.getId().toString());

        verify(recordRepository, never()).save(any());
    }
}