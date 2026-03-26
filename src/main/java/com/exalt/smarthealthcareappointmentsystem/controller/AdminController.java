package com.exalt.smarthealthcareappointmentsystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exalt.smarthealthcareappointmentsystem.dto.request.CreateDoctorRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.request.CreatePatientRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.DoctorResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.PatientResponse;
import com.exalt.smarthealthcareappointmentsystem.service.AdminService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/doctors")
    public ResponseEntity<DoctorResponse> createDoctor(@Valid @RequestBody CreateDoctorRequest request) {
        return new ResponseEntity<>(adminService.createDoctor(request), HttpStatus.CREATED);
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorResponse>> getDoctors() {
        return ResponseEntity.ok(adminService.getAllDoctors());
    }

    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable("id") Long doctorId) {
        adminService.deleteDoctorById(doctorId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/patients")
    public ResponseEntity<PatientResponse> createPatient(@Valid @RequestBody CreatePatientRequest request) {
        return new ResponseEntity<>(adminService.createPatient(request), HttpStatus.CREATED);
    }

    @GetMapping("/patients")
    public ResponseEntity<List<PatientResponse>> getPatients() {
        return ResponseEntity.ok(adminService.getAllPatients());
    }

    @DeleteMapping("/patients/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable("id") Long patientId) {
        adminService.deletePatientById(patientId);
        return ResponseEntity.noContent().build();
    }
}