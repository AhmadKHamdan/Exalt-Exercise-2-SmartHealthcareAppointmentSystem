package com.exalt.smarthealthcareappointmentsystem.service;

import com.exalt.smarthealthcareappointmentsystem.dto.request.CreateDoctorRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.DoctorResponse;

public interface AdminService {

    DoctorResponse createDoctor(CreateDoctorRequest request);
}
