package com.exalt.smarthealthcareappointmentsystem.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.exalt.smarthealthcareappointmentsystem.audit.LogAction;
import com.exalt.smarthealthcareappointmentsystem.dto.request.record.CreateRecordRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.record.RecordDetailsResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.record.RecordResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.record.RecordSummaryResponse;
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
    @LogAction("Record created")
    public RecordResponse createRecord(CreateRecordRequest request) {
        Doctor doctor = authenticationUtils.getAuthenticatedDoctor();

        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.patientId()));

        Appointment appointment = appointmentRepository.findById(request.appointmentId()).orElseThrow(
                () -> new ResourceNotFoundException("Appointment not found with id: " + request.appointmentId()));

        if (!appointment.getDoctor().getId().equals(doctor.getId())) {
            throw new AccessDeniedException("You are not allowed to create a record for this appointment.");
        }

        if (!appointment.getPatient().getId().equals(patient.getId())) {
            throw new InvalidRequestException("Appointment does not belong to the given patient.");
        }

        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new InvalidRequestException("A record can only be created for a completed appointment.");
        }

        Record record = recordMapper.toRecordEntity(request, doctor.getId());
        Record savedRecord = recordRepository.save(record);
        return recordMapper.toRecordResponse(savedRecord, patient, appointment);
    }

    @Override
    @LogAction("Record deleted")
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

        Patient patient = patientRepository.findById(record.getPatientId()).orElseThrow(
                () -> new ResourceNotFoundException("Patient not found with id: " + record.getPatientId()));

        Appointment appointment = appointmentRepository.findById(record.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appointment not found with id: " + record.getAppointmentId()));

        validateAccessToRecord(record);

        return recordMapper.toRecordDetailsResponse(record, doctor, patient, appointment);
    }

    private void validateAccessToRecord(Record record) {
        if (authenticationUtils.hasAuthority("ROLE_DOCTOR")) {
            Doctor currentDoctor = authenticationUtils.getAuthenticatedDoctor();
            if (currentDoctor.getId().equals(record.getDoctorId())) {
                return;
            }
        }

        if (authenticationUtils.hasAuthority("ROLE_PATIENT")) {
            Patient currentPatient = authenticationUtils.getAuthenticatedPatient();
            if (currentPatient.getId().equals(record.getPatientId())) {
                return;
            }
        }

        throw new AccessDeniedException("You are not allowed to access this record.");
    }

    @Override
    public List<RecordSummaryResponse> getRecordsForCurrentPatient() {
        Long patientId = authenticationUtils.getAuthenticatedPatient().getId();
        List<Record> records = recordRepository.findByPatientId(patientId);
        return records.stream().map(recordMapper::toRecordSummaryResponse).toList();
    }
}
