package com.suganya.fhirvitals.controller;

import com.suganya.fhirvitals.model.ObservationRecord;
import com.suganya.fhirvitals.service.FhirService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @RestController = this class exposes HTTP endpoints, and every method's
// return value gets automatically converted to JSON for the response.
@RestController
@RequestMapping("/api/observations")
public class ObservationController {

    private final FhirService fhirService;

    @Autowired
    public ObservationController(FhirService fhirService) {
        this.fhirService = fhirService;
    }

    // POST /api/observations/fetch/{patientId}
    // Triggers a live call to the FHIR server, then saves what it finds.
    @PostMapping("/fetch/{patientId}")
    public List<ObservationRecord> fetchForPatient(@PathVariable String patientId) {
        return fhirService.fetchAndSaveObservationsForPatient(patientId);
    }

    // GET /api/observations
    // Returns everything currently saved in our own local database.
    @GetMapping
    public List<ObservationRecord> getAll() {
        return fhirService.getAllSavedObservations();
    }
}