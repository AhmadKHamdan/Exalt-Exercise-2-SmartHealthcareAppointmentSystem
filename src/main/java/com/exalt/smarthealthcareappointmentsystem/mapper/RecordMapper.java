package com.exalt.smarthealthcareappointmentsystem.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.exalt.smarthealthcareappointmentsystem.dto.request.record.CreateRecordRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.appointment.AppointmentSummaryResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.doctor.DoctorSummaryResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.patient.PatientSummaryResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.record.RecordDetailsResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.record.RecordResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.record.RecordSummaryResponse;
import com.exalt.smarthealthcareappointmentsystem.entity.appointment.Appointment;
import com.exalt.smarthealthcareappointmentsystem.entity.appointment.Record;
import com.exalt.smarthealthcareappointmentsystem.entity.user.Doctor;
import com.exalt.smarthealthcareappointmentsystem.entity.user.Patient;

@Component
public class RecordMapper {

    public RecordResponse toRecordResponse(Record record, Patient patient, Appointment appointment) {
        return new RecordResponse(record.getId(), new PatientSummaryResponse(patient.getId(), patient.getFullName()),
                new AppointmentSummaryResponse(appointment.getId(), appointment.getAppointmentTime(),
                        appointment.getStatus()),
                record.getDiagnosis(), record.getPrescriptions(), record.getLabResults(), record.getPrescriptionDate());
    }

    public Record toRecordEntity(CreateRecordRequest request, Long doctorId) {
        return Record.builder().patientId(request.patientId()).doctorId(doctorId).appointmentId(request.appointmentId())
                .diagnosis(request.diagnosis()).prescriptions(request.prescriptions()).labResults(request.labResults())
                .prescriptionDate(LocalDateTime.now()).build();
    }

    public RecordDetailsResponse toRecordDetailsResponse(Record record, Doctor doctor, Patient patient,
            Appointment appointment) {
        return new RecordDetailsResponse(record.getId(),
                new DoctorSummaryResponse(doctor.getId(), doctor.getFullName(), doctor.getSpecialty()),
                new AppointmentSummaryResponse(appointment.getId(), appointment.getAppointmentTime(),
                        appointment.getStatus()),
                new PatientSummaryResponse(patient.getId(), patient.getFullName()), record.getDiagnosis(),
                record.getPrescriptions(), record.getLabResults(), record.getPrescriptionDate());
    }

    public RecordSummaryResponse toRecordSummaryResponse(Record record) {
        return new RecordSummaryResponse(record.getId(), record.getDoctorId(), record.getAppointmentId(),
                record.getDiagnosis(), record.getPrescriptions(), record.getLabResults(), record.getPrescriptionDate());
    }
}
