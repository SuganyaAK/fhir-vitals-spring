package com.suganya.fhirvitals.repository;

import com.suganya.fhirvitals.model.ObservationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

// By extending JpaRepository<ObservationRecord, Long>, Spring automatically
// generates a class behind the scenes that can:
//   save(...)      -> INSERT INTO observation_record ...
//   findAll()      -> SELECT * FROM observation_record
//   findById(id)   -> SELECT * FROM observation_record WHERE id = ?
//   deleteById(id) -> DELETE FROM observation_record WHERE id = ?
//  all without us writing a single line of SQL.

public interface ObservationRepository extends JpaRepository<ObservationRecord, Long> {
}