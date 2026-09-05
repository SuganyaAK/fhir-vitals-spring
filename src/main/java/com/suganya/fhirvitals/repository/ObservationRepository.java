package com.suganya.fhirvitals.repository;

import com.suganya.fhirvitals.model.ObservationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

// This interface has NO implementation written by us at all.
// By extending JpaRepository<ObservationRecord, Long>, Spring automatically
// generates a class behind the scenes that can:
//   save(...)      -> INSERT INTO observation_record ...
//   findAll()      -> SELECT * FROM observation_record
//   findById(id)   -> SELECT * FROM observation_record WHERE id = ?
//   deleteById(id) -> DELETE FROM observation_record WHERE id = ?
// ...and more, all without us writing a single line of SQL.
//
// The <ObservationRecord, Long> part means: "this repository manages
// ObservationRecord entities, whose primary key is of type Long."
public interface ObservationRepository extends JpaRepository<ObservationRecord, Long> {
}