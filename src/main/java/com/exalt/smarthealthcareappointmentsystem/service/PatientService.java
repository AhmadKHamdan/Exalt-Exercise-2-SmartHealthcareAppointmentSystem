package com.exalt.smarthealthcareappointmentsystem.service;

import java.util.List;

import com.exalt.smarthealthcareappointmentsystem.dto.request.patient.CreatePatientRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.request.patient.UpdatePatientRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.appointment.PatientAppointmentResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.patient.MyPatientProfileResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.patient.PatientResponse;

public interface PatientService {

    PatientResponse createPatient(CreatePatientRequest request);

    List<PatientResponse> getAllPatients();

    void deletePatientById(Long id);

    PatientResponse updatePatientById(UpdatePatientRequest request, Long id);

    List<PatientAppointmentResponse> getMyAppointments();

    MyPatientProfileResponse updateMyProfile(UpdatePatientRequest request);
}
