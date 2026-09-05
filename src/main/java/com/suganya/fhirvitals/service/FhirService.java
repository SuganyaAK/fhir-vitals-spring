package com.suganya.fhirvitals.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.suganya.fhirvitals.model.ObservationRecord;
import com.suganya.fhirvitals.repository.ObservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

// @Service marks this as a class holding business logic - the "what do
// we actually DO with the data" layer, separate from web-handling
// (Controller) and data-shape (Entity) concerns.
@Service
public class FhirService {

    private final WebClient webClient;
    private final ObservationRepository repository;

    // @Autowired: "Spring, please give me the WebClient bean and the
    // repository you already know how to build, and wire them into
    // this class automatically." We never call "new WebClient()" or
    // "new ObservationRepository()" ourselves - Spring handles that.
    @Autowired
    public FhirService(WebClient webClient, ObservationRepository repository) {
        this.webClient = webClient;
        this.repository = repository;
    }

    // Equivalent to your Python script's "query the endpoint to verify
    // if the record was created" step - but here, we also parse the
    // result and save each Observation into our local database.
    public List<ObservationRecord> fetchAndSaveObservationsForPatient(String patientId) {

        // This is the WebClient equivalent of:
        //   requests.get(f"{BASE_URL}/Observation?subject=Patient/{patient_id}")
        JsonNode response = webClient.get()
                .uri("/Observation?subject=Patient/{id}", patientId)
                .retrieve()
                .bodyToMono(JsonNode.class)
                // .block() waits for the response before continuing -
                // keeps this simple and synchronous, like Python's requests.
                .block();

        List<ObservationRecord> saved = new ArrayList<>();

        if (response == null || !response.has("entry")) {
            return saved; // no observations found for this patient
        }

        // FHIR "Bundle" responses list results under "entry" - loop through them
        for (JsonNode entry : response.get("entry")) {
            JsonNode resource = entry.get("resource");

            String fhirId = resource.path("id").asText();

            String code = resource.path("code")
                    .path("coding").get(0)
                    .path("display").asText("Unknown");

            String value = resource.path("valueQuantity").path("value").asText("N/A");
            String unit = resource.path("valueQuantity").path("unit").asText("");

            ObservationRecord record = new ObservationRecord(fhirId, patientId, code, value, unit);

            // repository.save(...) - this is the line that turns into
            // a real SQL INSERT statement, generated for us by Spring Data JPA.
            saved.add(repository.save(record));
        }

        return saved;
    }

    public List<ObservationRecord> getAllSavedObservations() {
        return repository.findAll();
    }
}