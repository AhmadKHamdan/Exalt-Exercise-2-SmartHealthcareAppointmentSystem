package com.exalt.smarthealthcareappointmentsystem.service;

import java.util.List;

import com.exalt.smarthealthcareappointmentsystem.dto.response.DoctorResponse;

public interface DoctorService {

    List<DoctorResponse> getDoctors(String specialty);

    DoctorResponse getDoctorById(Long id);
}
