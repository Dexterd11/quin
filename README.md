# Quin

Kotlin Spring Boot service that retrieves a symptom assessment and returns the
available contact options based on its urgency.

## Requirements

- Docker and Docker Compose
- GNU Make

For local execution without Docker, Java 17 or newer is also required.

### Installing GNU Make (If needed)

On macOS

```bash
brew install make
```

On Debian or Ubuntu Linux:

```bash
sudo apt-get update
sudo apt-get install build-essential
```

## Run with Docker

```bash
make start
```

The application starts on `http://localhost:8080`.

Stop the application with:

```bash
make stop
```

Other useful commands:

```bash
make logs
make status
make test
```

To run without Docker:

```bash
./gradlew bootRun
```

## API

Get the contact options for a symptom assessment:

```bash
curl http://localhost:8080/symptom-assessments/{id}/contact-options
```

Replace `{id}` with the assessment ID. The endpoint returns:

- `200 OK` with the assessment ID, urgency, and available contact options when the assessment exists.
- `404 Not Found` when the upstream client does not return an assessment.
- `500 I.s.e` Big change of : 429 - Too Many Requests. Refer: https://beeceptor.com/pricing/

Contact options are selected as follows:

- `HIGH`: `CALLBACK_NUMBER`, `PHONE_CALL`, and `CHAT` during the configured callback window; otherwise `PHONE_CALL` and `CHAT`.
- `MEDIUM`: `PHONE_CALL` and `CHAT`.
- `LOW`: `CHAT`.

The callback window is inclusive of the start time and exclusive of the end time,
using the server's local timezone.

## Configuration

Configuration is loaded from `src/main/resources/application.yml`:

```yaml
api:
  baseUrl: https://mp485036f870b872eef0.free.beeceptor.com

contact:
  callbackTimeFrom: 8
  callbackTimeTo: 12
```

The service listens on port `8080` by default. The API client uses the configured
`api.baseUrl` as the upstream assessment service.

## Assumptions

- There is one implicit practice; practice-specific configuration is out of scope.
- The server's local timezone is used for callback availability.
- The external triage platform is the source of truth for assessments, so no persistence layer is used.
- Contact options are returned as option types (`PHONE_CALL`, `CHAT`, and `CALLBACK_NUMBER`) without phone numbers or URLs.
- Authentication and authorization are out of scope.

## Tests

```bash
./gradlew test
```

The service unit tests are located at
`src/test/kotlin/com/example/quin/service/SymptomServiceTest.kt` and cover the
urgency rules, callback time boundaries, and missing assessments.

## Postman collection

Import `postman/Quin.postman_collection.json` into Postman to run requests for
all six assessment IDs from the assignment. The collection uses
`http://localhost:8080` as the default `baseUrl` variable.

## Project structure

- `config` contains Spring configuration properties loaded from `application.yml`.
- `controller` contains the HTTP API endpoints.
- `service` contains the contact-option business rules.
- `client` contains the upstream HTTP client integrations.
- `model` contains request and response domain models.
- `postman` contains the API request collection.
- `src/test` contains unit tests.

## With more time

- Add client timeouts, retries with exponential backoff.
- Add controller and integration tests for HTTP response status codes and external response mapping.

