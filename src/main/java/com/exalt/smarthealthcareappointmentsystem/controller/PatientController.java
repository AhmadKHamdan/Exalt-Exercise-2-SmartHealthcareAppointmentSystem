package com.exalt.smarthealthcareappointmentsystem.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exalt.smarthealthcareappointmentsystem.dto.request.patient.UpdatePatientRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.appointment.PatientAppointmentResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.patient.MyPatientProfileResponse;
import com.exalt.smarthealthcareappointmentsystem.service.AppointmentService;
import com.exalt.smarthealthcareappointmentsystem.service.PatientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Tag(name = "Patient")
public class PatientController {

    private final PatientService patientService;
    private final AppointmentService appointmentService;

    @Operation(summary = "Retrieve all appointments for the current patient")
    @GetMapping("/me/appointments")
    public ResponseEntity<List<PatientAppointmentResponse>> getMyAppointments() {
        return ResponseEntity.ok(appointmentService.getAppointmentsForCurrentPatient());
    }

    @Operation(summary = "Update current patient's profile information")
    @PutMapping("/me")
    public ResponseEntity<MyPatientProfileResponse> updateMe(@Valid @RequestBody UpdatePatientRequest request) {
        return ResponseEntity.ok(patientService.updateMyProfile(request));
    }
}
