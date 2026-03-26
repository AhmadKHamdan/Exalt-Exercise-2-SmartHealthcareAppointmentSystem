package com.exalt.smarthealthcareappointmentsystem.service;

import java.util.List;

import com.exalt.smarthealthcareappointmentsystem.dto.request.CreateDoctorRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.DoctorResponse;

public interface AdminService {

    DoctorResponse createDoctor(CreateDoctorRequest request);

    List<DoctorResponse> getAllDoctors();
}
