package com.exalt.smarthealthcareappointmentsystem.service;

import com.exalt.smarthealthcareappointmentsystem.dto.request.record.CreateRecordRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.record.RecordResponse;

public interface RecordService {

    RecordResponse createRecord(CreateRecordRequest request);

    void deleteRecordById(String id);

    // RecordResponse updateRecordById(UpdateRecordRequest request, Long recordId);

    // RecordResponse getRecordById();

    // List<PatientRecordResponse> getRecordsForCurrentPatient();

    // List<DoctorRecordResponse> getRecordsForCurrentDoctor();

    // List<AppointmentRecordResponse> getRecordsByAppointment();
}
