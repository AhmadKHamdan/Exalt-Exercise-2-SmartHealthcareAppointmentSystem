package com.exalt.smarthealthcareappointmentsystem.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.exalt.smarthealthcareappointmentsystem.dto.request.record.CreateRecordRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.record.RecordDetailsResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.record.RecordResponse;
import com.exalt.smarthealthcareappointmentsystem.entity.appointment.Appointment;
import com.exalt.smarthealthcareappointmentsystem.entity.appointment.Record;
import com.exalt.smarthealthcareappointmentsystem.entity.user.Doctor;
import com.exalt.smarthealthcareappointmentsystem.entity.user.Patient;
import com.exalt.smarthealthcareappointmentsystem.exception.AccessDeniedException;
import com.exalt.smarthealthcareappointmentsystem.exception.ResourceNotFoundException;
import com.exalt.smarthealthcareappointmentsystem.mapper.RecordMapper;
import com.exalt.smarthealthcareappointmentsystem.repository.AppointmentRepository;
import com.exalt.smarthealthcareappointmentsystem.repository.DoctorRepository;
import com.exalt.smarthealthcareappointmentsystem.repository.PatientRepository;
import com.exalt.smarthealthcareappointmentsystem.repository.RecordRepository;
import com.exalt.smarthealthcareappointmentsystem.service.RecordService;
import com.exalt.smarthealthcareappointmentsystem.util.AuthenticationUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final RecordMapper recordMapper;
    private final AuthenticationUtils authenticationUtils;

    @Override
    public RecordResponse createRecord(CreateRecordRequest request) {
        Long doctorId = authenticationUtils.getAuthenticatedDoctor().getId();
        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.patientId()));

        Appointment appointment = appointmentRepository.findById(request.appointmentId()).orElseThrow(
                () -> new ResourceNotFoundException("Appointment not found with id: " + request.appointmentId()));

        Record record = recordMapper.toRecordEntity(request, doctorId);
        Record savedRecord = recordRepository.save(record);
        return recordMapper.toRecordResponse(savedRecord, patient, appointment);
    }

    @Override
    public void deleteRecordById(String id) {
        Long doctorId = authenticationUtils.getAuthenticatedDoctor().getId();
        List<Record> records = recordRepository.findByDoctorId(doctorId);
        Record record = records.stream().filter(rec -> rec.getId().equals(id)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));

        recordRepository.delete(record);
    }

    @Override
    public RecordDetailsResponse getRecordById(String id) {
        Record record = recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));

        Doctor doctor = doctorRepository.findById(record.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + record.getDoctorId()));

        Patient patient = patientRepository.findById(record.getPatientId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Patient not found with id: " + record.getPatientId()));

        Appointment appointment = appointmentRepository.findById(record.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appointment not found with id: " + record.getAppointmentId()));

        if (!isDoctorOwner(record, doctor) && !isPatientOwner(record, patient)) {
            throw new AccessDeniedException("You are not allowed to access this record");
        }

        return recordMapper.toRecordDetailsResponse(record, doctor, patient, appointment);
    }

    private boolean isPatientOwner(Record record, Patient patient) {
        if (!authenticationUtils.getAuthenticatedAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_PATIENT"))) {
            return false;
        }

        return patient.getId().equals(record.getPatientId());
    }

    private boolean isDoctorOwner(Record record, Doctor doctor) {
        if (!authenticationUtils.getAuthenticatedAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_DOCTOR"))) {
            return false;
        }

        return doctor.getId().equals(record.getDoctorId());
    }
}
