package com.exalt.smarthealthcareappointmentsystem.service;

import com.exalt.smarthealthcareappointmentsystem.dto.request.record.CreateRecordRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.record.RecordResponse;

public interface RecordService {

    RecordResponse createRecord(CreateRecordRequest request);

    // RecordResponse updateRecordById(UpdateRecordRequest request, Long recordId);

    // void deleteRecordById(Long id);

    // RecordResponse getRecordById();

    // List<PatientRecordResponse> getRecordsForCurrentPatient();

    // List<DoctorRecordResponse> getRecordsForCurrentDoctor();

    // List<AppointmentRecordResponse> getRecordsByAppointment();
}
