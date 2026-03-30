package com.exalt.smarthealthcareappointmentsystem.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.exalt.smarthealthcareappointmentsystem.entity.appointment.Record;

@Repository
public interface RecordRepository extends MongoRepository<Record, String> {

    List<Record> getByDoctorId(Long doctorId);
}
