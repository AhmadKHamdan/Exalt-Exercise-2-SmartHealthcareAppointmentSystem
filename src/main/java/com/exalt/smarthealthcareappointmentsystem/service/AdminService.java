package com.exalt.smarthealthcareappointmentsystem.service;

import java.util.List;

import com.exalt.smarthealthcareappointmentsystem.dto.request.doctor.CreateDoctorRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.request.doctor.UpdateDoctorRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.request.patient.CreatePatientRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.request.patient.UpdatePatientRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.appointment.AppointmentResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.doctor.DoctorResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.patient.PatientResponse;

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
