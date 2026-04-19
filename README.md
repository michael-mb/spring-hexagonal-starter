# spring-starter

A reusable Spring Boot starter template built with hexagonal architecture. Includes an `account` module with JWT authentication via Keycloak, ready to clone and extend for new cozisoft applications.

## Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 25 |
| Framework | Spring Boot 4.0.2 |
| Build | Gradle 9 (multi-project) |
| Auth | Keycloak 26 (Docker) |
| Database | PostgreSQL 16 (Docker) |
| Migrations | Liquibase (per-service schema) |
| API spec | OpenAPI 3 → code generated at compile time |
| Tests | JUnit 5 + TestContainers + WireMock |

## Project structure

```
spring-starter/
├── gateway/          # Spring Boot app — HTTP layer, security, facades
├── services/
│   └── account/      # Account domain — hexagonal core + Keycloak infra
├── shared/           # Shared utilities, base classes, test fixtures
├── openapi/
│   └── api-spec.yaml # OpenAPI spec (source of truth for controllers)
└── local-setup/
    ├── compose.yaml  # Postgres 16 + Keycloak 26
    ├── scripts/
    │   └── init-db.sql
    └── keycloak/
        └── realm-export.json
```

### Architecture (hexagonal)

Each service (`account`, and future services) follows a ports & adapters pattern:

```
gateway/
└── business/
    ├── api/rest/          ← HTTP controllers (implements generated OpenAPI interface)
    ├── core/
    │   ├── AccountManagementFacadeImpl   ← orchestration + authorization
    │   └── port/in/AccountManagementFacade
    └── config/GatewayConfiguration       ← wires facade beans

services/account/
└── core/
    ├── AccountManager                    ← domain logic
    ├── model/entities/                   ← JPA entities
    ├── model/payload/                    ← request/response DTOs
    └── port/
        ├── in/AccountManagement          ← inbound port (interface)
        └── out/
            ├── AccountRepository         ← outbound port
            └── IdPUserManagementAdapter  ← Keycloak outbound port
    infra/
    ├── AccountRepositoryImpl
    ├── db/JpaAccountRepository
    └── keycloak/
        ├── CustomKeycloakAdminClient     ← Keycloak Admin REST API
        └── DefaultIdPUserManagementAdapter
```

## Prerequisites

- **Java 25** — [Download](https://jdk.java.net/25/)
- **Docker Desktop** — containers are managed automatically by Spring Boot

## Setup

### 1. Clone and configure environment

```bash
cp .env.example .env
```

The default `.env` values work out of the box with the local Docker setup — no edits needed for local development.

### 2. Run

```bash
./gradlew :gateway:bootRun
```

Spring Boot Docker Compose support starts Postgres and Keycloak automatically on first run. The app is available at `http://localhost:8081/api`.

> **First startup is slow (~60s)** — Keycloak needs time to initialize and import the realm.

### 3. Get an access token

```bash
# As admin
curl -s -X POST http://localhost:8080/realms/starter/protocol/openid-connect/token \
  -d 'grant_type=password&client_id=starter-app&username=admin@starter.io&password=password123' \
  | jq -r '.access_token'

# As regular user
curl -s -X POST http://localhost:8080/realms/starter/protocol/openid-connect/token \
  -d 'grant_type=password&client_id=starter-app&username=user@starter.io&password=password123' \
  | jq -r '.access_token'
```

Export the token for use in subsequent requests:

```bash
export TOKEN=<paste token here>
```

## API

Base URL: `http://localhost:8081/api`  
Swagger UI: `http://localhost:8081/api/swagger-ui.html`

| Method | Path | Role | Description |
|--------|------|------|-------------|
| `POST` | `/accounts` | Any | Register account for the authenticated user |
| `GET` | `/accounts/whoami` | Any | Get your own account |
| `GET` | `/accounts/{id}` | Any | Get account by ID |
| `PATCH` | `/accounts/{id}` | Owner | Update your own account |
| `DELETE` | `/accounts/{id}` | ADMIN | Delete an account |
| `PUT` | `/accounts/{id}/status` | ADMIN | Suspend or activate an account |

### Example requests

```bash
# Create your account
curl -X POST http://localhost:8081/api/accounts \
  -H "Authorization: Bearer $TOKEN"

# Get your account
curl http://localhost:8081/api/accounts/whoami \
  -H "Authorization: Bearer $TOKEN"

# Patch your account
curl -X PATCH http://localhost:8081/api/accounts/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"firstName": "John", "lastName": "Doe", "email": "john@example.com"}'

# Suspend an account (ADMIN token required)
curl -X PUT http://localhost:8081/api/accounts/1/status \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"action": "suspend"}'
```

## Local Keycloak

| URL | `http://localhost:8080` |
|-----|------------------------|
| Admin console | `http://localhost:8080/admin` (admin / admin) |
| Realm | `starter` |
| Client | `starter-app` (public, direct access grants) |

**Pre-configured users:**

| Email | Password | Role |
|-------|----------|------|
| `admin@starter.io` | `password123` | ADMIN |
| `user@starter.io` | `password123` | USER |

## Running tests

```bash
./gradlew :gateway:test
```

Tests use TestContainers (PostgreSQL) and WireMock (OIDC endpoints) — Docker must be running. The Spring context starts in ~5s for subsequent runs thanks to context caching.

### Test architecture

```
shared/src/testFixtures/          ← reusable across all future services
├── TestResourceExtension         # starts WireMock + RSA key before Spring context
├── ServiceModuleTest             # @SpringBootTest meta-annotation
├── BaseApiTest                   # MockMvc wrapper + authentication() helper
├── DbResetService                # truncates *_service schemas between tests
└── RequestParameters             # fluent HTTP query param builder

gateway/src/test/
├── GatewayBaseApiTest            # base for all gateway tests (TC Postgres, mocks)
├── CoreTestConfiguration         # test beans (client, fixtures, db reset)
├── StarterRestTestClient         # typed HTTP client for account endpoints
├── FixtureProvider               # loads JSON fixtures from classpath
└── api/rest/AccountManagementApiTest  # 9 integration tests
```

Authentication in tests uses Spring Security's `authentication()` post-processor — JWTs are never decoded, so no Keycloak connection is needed at test time.

## Ports

| Service | Port |
|---------|------|
| App | 8081 |
| Actuator | 8091 |
| Keycloak | 8080 |
| PostgreSQL | 5432 |

Actuator health: `http://localhost:8091/api/actuator/health`

## Adding a new service

1. Create `services/<name>/` with the same structure as `services/account/`
2. Add `@ConditionalOnProperty(name = "services.<name>.enabled", havingValue = "true")` to its `*ServiceConfiguration`
3. Register it in `gateway/business/config/GatewayConfiguration.java`
4. Add Liquibase config to `application-development.yaml` under `services.<name>.liquibase`
5. Add `@MockitoBean` for any new outbound ports in `GatewayBaseApiTest`
