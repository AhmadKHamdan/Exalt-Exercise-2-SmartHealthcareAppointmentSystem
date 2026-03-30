package com.exalt.smarthealthcareappointmentsystem.service;

import com.exalt.smarthealthcareappointmentsystem.dto.request.record.CreateRecordRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.record.RecordDetailsResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.record.RecordResponse;

public interface RecordService {

    RecordResponse createRecord(CreateRecordRequest request);

    void deleteRecordById(String id);

    RecordDetailsResponse getRecordById(String id);    
}
