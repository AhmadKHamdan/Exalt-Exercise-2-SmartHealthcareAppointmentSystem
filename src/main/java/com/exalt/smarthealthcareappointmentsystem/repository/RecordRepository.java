package com.exalt.smarthealthcareappointmentsystem.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.exalt.smarthealthcareappointmentsystem.entity.appointment.Record;

@Repository
public interface RecordRepository extends MongoRepository<Record, String> {

}
