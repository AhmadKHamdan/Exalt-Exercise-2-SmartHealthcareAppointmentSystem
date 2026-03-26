package com.exalt.smarthealthcareappointmentsystem.service;

import java.util.List;

import com.exalt.smarthealthcareappointmentsystem.dto.request.CreateDoctorRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.request.CreatePatientRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.DoctorResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.PatientResponse;

public interface AdminService {

    DoctorResponse createDoctor(CreateDoctorRequest request);

    void deleteDoctorById(Long id);

    PatientResponse createPatient(CreatePatientRequest request);

    List<PatientResponse> getAllPatients();

    void deletePatientById(Long id);
}
