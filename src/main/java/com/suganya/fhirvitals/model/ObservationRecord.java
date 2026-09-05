package com.suganya.fhirvitals.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

// @Entity tells Spring this Java class represents a table in the database
@Entity
public class ObservationRecord {

    // @Id marks the primary key. @GeneratedValue means the database
    // auto-assigns the number
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The Observation's ID as it exists on the FHIR server
    private String fhirObservationId;

    // Which patient this observation belongs to
    private String patientId;

    // What was measured (e.g. "Heart rate", "Body temperature")
    private String observationCode;

    // "Value" cannot be used as a field name as it is reseverd in JAva
    @Column(name = "measurement_value")
    private String value;

    private String unit;

    // JPA requires a no-argument constructor - it uses this internally
    // when reading rows back out of the database.
    public ObservationRecord() {
    }

    public ObservationRecord(String fhirObservationId, String patientId,
                              String observationCode, String value, String unit) {
        this.fhirObservationId = fhirObservationId;
        this.patientId = patientId;
        this.observationCode = observationCode;
        this.value = value;
        this.unit = unit;
    }

    // Getters and setters - Spring/JPA use these to read and write each field.

    public Long getId() {
        return id;
    }

    public String getFhirObservationId() {
        return fhirObservationId;
    }

    public void setFhirObservationId(String fhirObservationId) {
        this.fhirObservationId = fhirObservationId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getObservationCode() {
        return observationCode;
    }

    public void setObservationCode(String observationCode) {
        this.observationCode = observationCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}