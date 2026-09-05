package com.suganya.fhirvitals.controller;

import com.suganya.fhirvitals.service.FhirService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// @RestController = this class exposes HTTP endpoints, and every method's
// return value gets automatically converted to JSON for the response.
@RestController
@RequestMapping("/api/patient")
public class PatientController {

    private final FhirService fhirService;

    @Autowired
    public PatientController(FhirService fhirService) {
        this.fhirService = fhirService;
    }

    // POST /api/patient/create
    // Creates a new Patient resource on the FHIR server and returns its assigned ID.

    @PostMapping("/create")
    public String createPatient() {
        return fhirService.createPatient();
    }
}