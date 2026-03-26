package com.exalt.smarthealthcareappointmentsystem.service;

import java.util.List;

import com.exalt.smarthealthcareappointmentsystem.dto.request.CreateDoctorRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.request.CreatePatientRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.request.UpdateDoctorRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.request.UpdatePatientRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.AppointmentResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.DoctorResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.PatientResponse;

public interface AdminService {

    DoctorResponse createDoctor(CreateDoctorRequest request);

    void deleteDoctorById(Long id);

    DoctorResponse updateDoctorById(UpdateDoctorRequest request, Long doctorId);

    PatientResponse createPatient(CreatePatientRequest request);

    List<PatientResponse> getAllPatients();

    void deletePatientById(Long id);

    PatientResponse updatePatientById(UpdatePatientRequest request, Long id);

    List<AppointmentResponse> getAllAppointments();
}
