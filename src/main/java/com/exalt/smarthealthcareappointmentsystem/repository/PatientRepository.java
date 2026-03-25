package com.exalt.smarthealthcareappointmentsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exalt.smarthealthcareappointmentsystem.entity.user.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

}