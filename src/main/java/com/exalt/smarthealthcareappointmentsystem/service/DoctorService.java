package com.exalt.smarthealthcareappointmentsystem.service;

import java.util.List;

import com.exalt.smarthealthcareappointmentsystem.dto.request.doctor.CreateDoctorRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.request.doctor.UpdateDoctorRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.appointment.DoctorAppointmentResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.doctor.DoctorResponse;

public interface DoctorService {

    List<DoctorResponse> getDoctors(String specialty);

    DoctorResponse getDoctorById(Long id);

    DoctorResponse createDoctor(CreateDoctorRequest request);

    void deleteDoctorById(Long id);

    DoctorResponse updateDoctorById(UpdateDoctorRequest request, Long doctorId);

    List<DoctorAppointmentResponse> getMyAppointments();
}
