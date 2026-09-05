package com.suganya.fhirvitals.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.suganya.fhirvitals.model.ObservationRecord;
import com.suganya.fhirvitals.repository.ObservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

// @Service - class holding business logic separate from web-handling
// (Controller) and data-shape (Entity) concerns.
@Service
public class FhirService {

    private final WebClient webClient;
    private final ObservationRepository repository;

    // @Autowired: gives the WebClient bean and the
    // repository and wire them into
    // this class automatically.
    @Autowired
    public FhirService(WebClient webClient, ObservationRepository repository) {
        this.webClient = webClient;
        this.repository = repository;
    }

    public String createPatient(){
        
        Map<String,Object> nameData = new HashMap<>();
        nameData.put("use", "official");
        nameData.put("family", "Kumar");
        nameData.put("given",List.of("Meena"));
        nameData.put("prefix", List.of("Mrs"));

        Map<String,Object> telecomData = new HashMap<>();   
        telecomData.put("system","email");
        telecomData.put("value", "meenaraju@example.com"); 

        Map<String,Object> addressData = new HashMap<>();   
        addressData.put("use","home");
        addressData.put("type", "postal");
        addressData.put("line", List.of("123 Main St"));    
        addressData.put("city", "Anytown");
        addressData.put("state", "MS"); 

        // Map<String,Object> generalPractitionerData = new HashMap<>();  
        // generalPractitionerData.put("reference", "Practitioner/137767593"); 

        Map<String,Object> patientData = new HashMap<>();
        patientData.put("resourceType", "Patient");
        patientData.put("active", true);
        patientData.put("name", List.of(nameData));
        patientData.put("telecom", List.of(telecomData));
        patientData.put("gender", "female");
        patientData.put("birthDate", "1982-05-29");
        patientData.put("address", List.of(addressData));
        // patientData.put("generalPractitioner", List.of(generalPractitionerData));
        
        JsonNode response = webClient.post()
                .uri("/Patient")
                .bodyValue(patientData)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
        return response.path("id").asText();
    }
    // query the endpoint to verify if the record was created
    // also parse the result and save each Observation into our local database.
    public List<ObservationRecord> fetchAndSaveObservationsForPatient(String patientId) {

        // This is the WebClient equivalent to
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
            // a real SQL INSERT statement, generated for by Spring Data JPA.
            saved.add(repository.save(record));
        }

        return saved;
    }

    public List<ObservationRecord> getAllSavedObservations() {
        return repository.findAll();
    }
}