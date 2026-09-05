# FHIR Vitals — Spring Boot Edition

A small Spring Boot app that talks to a live FHIR server: it can create Patients, Practitioners, and
Observations, and it fetches Observations back and saves them into a local database.

This is the Java/Spring rebuild of an earlier Python project I wrote that did the same thing using
`requests` and plain scripts. I built this version to actually *learn* Java and Spring Boot properly,
not just read about them, every piece here started as something I didn't fully understand and had to
work through, error message by error message.

## What it actually does

- **Creates a Patient** on a public FHIR test server, from a full FHIR-shaped payload (name, contact
  info, address, marital status, linked practitioner, etc.)
- **Fetches existing Observations** for a given Patient ID directly from the FHIR server
- **Saves fetched Observations into a local SQL database**, so they persist across restarts

In short: it's a small but real demonstration of consuming *and* producing FHIR resources over REST,
plus persisting data locally.

## Tech stack

- **Java 17**
- **Spring Boot 3** — Spring Web (to expose my own endpoints), Spring WebFlux's `WebClient` (to call
  the FHIR server), Spring Data JPA (to talk to the database without writing raw SQL)
- **H2** — a lightweight, file-based SQL database, good for a project like this where spinning up
  Postgres would be overkill
- **HAPI FHIR public test server** (`hapi.fhir.org/baseR4`) as the external FHIR API

## Project structure

```
src/main/java/com/suganya/fhirvitals/
├── FhirVitalsApplication.java     # entry point
├── config/
│   └── WebClientConfig.java       # sets up the WebClient used to call the FHIR server
├── model/
│   └── ObservationRecord.java     # the shape of an Observation as saved in our own database
├── repository/
│   └── ObservationRepository.java # database access - no SQL written by hand
├── service/
│   └── FhirService.java           # the actual logic: calling FHIR, parsing responses, saving data
└── controller/
    ├── PatientController.java     # HTTP endpoints for creating Patients
    └── ObservationController.java # HTTP endpoints for Observations
```

## Running it locally

You'll need Java 17 and Maven installed.

```bash
mvn spring-boot:run
```

Once you see `Started FhirVitalsApplication`, the app is live on `http://localhost:8080`.

## API endpoints

| Method | Path                          | What it does                                             |
|--------|-------------------------------|-----------------------------------------------------------|
| POST   | `/api/patient/create`         | Creates a detailed Patient on the FHIR server             |
| POST   | `/api/observations/fetch/{patientId}` | Fetches existing Observations for a Patient from the FHIR server and saves them locally |
| GET    | `/api/observations`           | Lists everything currently saved in the local database    |

### Example: full workflow

```bash
# 1. Create a patient
curl -X POST http://localhost:8080/api/patient/create

# 2. See what's saved
curl http://localhost:8080/api/observations
```

## Things I ran into while building this (and actually learned from)

A few real bugs I hit and had to figure out — leaving these here since they were genuinely useful
lessons, not just annoyances:

- **`value` is a reserved SQL keyword.** Naming a database column `value` broke every query with a
  cryptic syntax error, until I renamed the underlying column via `@Column(name = "measurement_value")`
  while keeping the Java field name as `value`.
- **A stale ID on a shared public test server.** I once spent a while confused about "empty results"
  before realizing I was searching by an Observation ID instead of the Patient ID it was linked to —
  a one-digit difference that cost real debugging time.
- **A transient `412 Precondition Failed`.** A resource reference that *did* exist (confirmed via
  curl) still got rejected once by the FHIR server, most likely due to the flakiness of a shared public
  test server under constant use by other developers worldwide. A good reminder that real systems need
  to handle transient upstream failures gracefully, not assume every call behaves identically.

## Possible next steps

- Create `Practitioner` endpoints into their own `PractitionerController`, matching the pattern used for
  `PatientController`
- Add proper error handling around the FHIR calls (currently a failed call just throws a generic
  exception)
- Containerize with Docker
- Add a GitHub Actions CI pipeline

## Related project

The original Python version of this same idea lives at
[fhir-vitals-dashboard](https://github.com/SuganyaAK/fhir-vitals-dashboard).

---

Built by [Suganya Raju](https://github.com/SuganyaAK) while learning Java and Spring Boot from scratch.