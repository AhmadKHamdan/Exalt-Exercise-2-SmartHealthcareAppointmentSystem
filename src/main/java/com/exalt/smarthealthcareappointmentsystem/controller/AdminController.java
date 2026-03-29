package com.exalt.smarthealthcareappointmentsystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exalt.smarthealthcareappointmentsystem.dto.request.doctor.CreateDoctorRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.request.doctor.UpdateDoctorRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.request.patient.CreatePatientRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.request.patient.UpdatePatientRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.appointment.AppointmentResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.doctor.DoctorResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.patient.PatientResponse;
import com.exalt.smarthealthcareappointmentsystem.service.AdminService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Management")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "Create a new doctor")
    @PostMapping("/doctors")
    public ResponseEntity<DoctorResponse> createDoctor(@Valid @RequestBody CreateDoctorRequest request) {
        return new ResponseEntity<>(adminService.createDoctor(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a doctor")
    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        adminService.deleteDoctorById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update a doctor")
    @PutMapping("/doctors/{id}")
    public ResponseEntity<DoctorResponse> updateDoctor(@Valid @RequestBody UpdateDoctorRequest request,
            @PathVariable Long id) {
        DoctorResponse response = adminService.updateDoctorById(request, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Create a new patient")
    @PostMapping("/patients")
    public ResponseEntity<PatientResponse> createPatient(@Valid @RequestBody CreatePatientRequest request) {
        return new ResponseEntity<>(adminService.createPatient(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Get all patients")
    @GetMapping("/patients")
    public ResponseEntity<List<PatientResponse>> getPatients() {
        return ResponseEntity.ok(adminService.getAllPatients());
    }

    @Operation(summary = "Delete a patient")
    @DeleteMapping("/patients/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        adminService.deletePatientById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update a patient")
    @PutMapping("/patients/{id}")
    public ResponseEntity<PatientResponse> updatePatient(@Valid @RequestBody UpdatePatientRequest request,
            @PathVariable Long id) {
        PatientResponse response = adminService.updatePatientById(request, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get all appointments")
    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponse>> getAppointments() {
        return ResponseEntity.ok(adminService.getAllAppointments());
    }
}