# spring-starter — Contexte & Plan

Ce projet est un **starter vierge** basé sur l'architecture de `kassi-rh-backend` (cozisoft),
réduit au strict minimum : module `account` uniquement, avec **Keycloak** en Docker à la place d'Auth0.

L'objectif est d'avoir une base réutilisable pour toutes les futures applications cozisoft.

---

## Décisions validées

| Aspect | Choix |
|--------|-------|
| Nom du projet | `spring-starter` (à confirmer) |
| Base package | `com.cozisoft.starter` (à confirmer) |
| Auth provider | Keycloak 26 (Docker local, remplace Auth0) |
| JWT roles claim | `realm_access.roles` (Keycloak natif) |
| User principal | `AppUser` (simplifié, pas d'org context) |
| IdP Management API | Supprimé — `sub` JWT suffit |
| Object storage | Supprimé (MinIO non inclus) |
| Org filter | Supprimé (`OrganizationResolutionFilter`) |
| Services inclus | Account uniquement |
| Stack | Java 25, Spring Boot 4.0.2, Gradle multi-project |

---

## Structure cible

```
spring-starter/
├── settings.gradle
├── build.gradle               # Java 25, Spring Boot 4.0.2, OpenAPI Generator 7.x
├── dependencies.gradle        # versions centralisées
├── gradlew / gradlew.bat
├── .env.example
├── .gitignore
├── openapi/
│   └── api-spec.yaml          # spec account seulement (6 endpoints)
├── gateway/
│   ├── build.gradle
│   └── src/main/java/com/cozisoft/starter/
│       ├── Application.java
│       ├── config/
│       │   ├── MasterConfiguration.java
│       │   ├── SecurityConfiguration.java      # JWT Keycloak
│       │   ├── AsyncConfiguration.java
│       │   └── SystemConfigurationProperties.java
│       ├── business/config/
│       │   └── GatewayConfiguration.java       # beans account seulement
│       └── core/security/
│           ├── CustomJwtAuthenticationConverter.java  # lit realm_access.roles
│           ├── CustomJwtAuthenticationToken.java
│           └── AppUser.java                    # remplace OrganizationUser
│   └── src/main/resources/
│       ├── application.yaml
│       └── application-development.yaml
├── services/account/
│   ├── build.gradle
│   └── src/main/java/com/cozisoft/starter/account/
│       ├── config/
│       │   ├── AccountServiceConfiguration.java
│       │   ├── AccountServiceDataLayerConfiguration.java
│       │   └── AccountDomainConfiguration.java
│       ├── core/
│       │   ├── AccountManager.java             # PAS de IdPUserManagementAdapter
│       │   ├── model/entities/
│       │   │   ├── Account.java
│       │   │   ├── AccountAddress.java
│       │   │   ├── AccountStatus.java
│       │   │   ├── AccountCreationState.java
│       │   │   └── Gender.java
│       │   ├── model/payload/
│       │   │   ├── CreateAccountRequest.java
│       │   │   └── PatchAccountRequest.java
│       │   └── port/
│       │       ├── in/AccountManagement.java
│       │       └── out/AccountRepository.java
│       └── infra/
│           ├── AccountRepositoryImpl.java
│           └── db/JpaAccountRepository.java
│   └── src/main/resources/db/account/
│       ├── db.changelog-master.xml
│       └── migrations/
│           ├── V00001__create_initial_tables.sql
│           └── V00002__add_gender_to_account.sql
│   └── src/test/java/com/cozisoft/starter/account/
│       └── AccountApiTest.java                 # TestContainers + JWT mocké
├── shared/
│   ├── build.gradle
│   └── src/main/java/com/cozisoft/starter/shared/
│       ├── config/
│       │   ├── KeycloakProperties.java          # remplace Auth0ManagementApiProperties
│       │   ├── ServiceConfigurationProperties.java
│       │   └── ServiceDataLayerBaseConfiguration.java
│       ├── core/
│       │   ├── CustomSecurityContextHolder.java
│       │   ├── PhoneNumberHelper.java
│       │   ├── StringUtils.java
│       │   ├── DateUtils.java
│       │   ├── ObjectMapperUtils.java
│       │   ├── EqualsAndHashCodeUtils.java
│       │   ├── errorhandling/
│       │   │   ├── ErrorCodes.java
│       │   │   ├── ManagerErrorException.java
│       │   │   ├── ManagerErrorResponse.java
│       │   │   ├── GlobalExceptionHandler.java
│       │   │   └── exceptions/
│       │   │       ├── DataAlreadyExistsException.java
│       │   │       ├── DataDoesNotExistException.java
│       │   │       ├── InvalidStatusException.java
│       │   │       ├── InvalidSubmissionException.java
│       │   │       └── NoPermissionException.java
│       │   ├── exception/
│       │   │   └── DataIntegrityViolationException.java
│       │   ├── model/
│       │   │   ├── db/
│       │   │   │   ├── BaseEntity.java
│       │   │   │   ├── GenericAddress.java
│       │   │   │   ├── PhoneNumber.java
│       │   │   │   ├── PhoneNumberConverter.java
│       │   │   │   ├── DatabaseColumnConstraintKind.java
│       │   │   │   └── GenericObjectValidator.java
│       │   │   └── payload/
│       │   │       ├── GenericAddressPayload.java
│       │   │       ├── DatabaseColumnConstraintDefinition.java
│       │   │       └── DataIntegrityConstraintViolation.java
│       │   └── port/out/
│       │       └── DefaultCrudRepository.java
│       └── infra/
│           └── DefaultCrudRepositoryImpl.java
└── local-dev/
    ├── compose.yaml                # Postgres 16 + Keycloak 26
    ├── scripts/
    │   └── init-db.sql             # CREATE SCHEMA account_service
    └── keycloak/
        └── realm-export.json       # realm "starter" préconfiguré
```

---

## Endpoints account (OpenAPI)

```
POST   /api/accounts              → créer account (idpUserId = sub du JWT)
GET    /api/accounts/whoami       → account de l'utilisateur connecté
GET    /api/accounts/{accountId}  → get par ID
PATCH  /api/accounts/{accountId}  → mise à jour (firstName, lastName, email, gender, phone, address)
DELETE /api/accounts/{accountId}  → suppression (soft delete)
PUT    /api/accounts/{accountId}/status → suspend / activate
```

---

## Infrastructure Docker (local-dev/compose.yaml)

```
Postgres 16  → localhost:5432  (DB: starter_db, user: starter_user, pwd: secret)
Keycloak 26  → http://localhost:8080  (admin/admin)
```

### Keycloak — realm "starter" préconfiguré
- Client `starter-app` (public, direct access grants activés)
- Users : `admin@starter.io` / `user@starter.io` (password: `password123`)
- Rôles : `ADMIN`, `USER`

**Obtenir un token :**
```bash
curl -X POST http://localhost:8080/realms/starter/protocol/openid-connect/token \
  -d 'grant_type=password&client_id=starter-app&username=admin@starter.io&password=password123'
```

---

## Ports applicatifs (dev)

| Service | Port |
|---------|------|
| App (dev) | 8081 |
| Management/actuator (dev) | 8091 |
| Keycloak | 8080 |
| Postgres | 5432 |

---

## Lancement

```bash
./gradlew :gateway:bootRun
# Docker Compose démarre Postgres + Keycloak automatiquement
# Spring Boot démarre sur http://localhost:8081/api
```

---

## Tests

`AccountApiTest` couvre :
- Création d'un account (`POST /accounts`)
- `whoami` (`GET /accounts/whoami`)
- Get par ID
- Patch
- Delete
- Changement de status (suspend / activate)

Stack de test : `@SpringBootTest` + `@AutoConfigureMockMvc` + TestContainers (Postgres) + JWT mocké via Spring Security Test.

---

## Source de référence

Architecture copiée depuis : `/Users/michael/Documents/dev/cozisoft/kassi-rh-backend`

Patterns identiques :
- Hexagonal (ports & adapters) dans chaque service
- OpenAPI Generator → interfaces Spring générées à la compilation
- Liquibase par service (schéma dédié `account_service`)
- `ServiceDataLayerBaseConfiguration` pour configurer Liquibase par service
- `DefaultCrudRepository` générique
- `GlobalExceptionHandler` + `ManagerErrorException`
- `CustomSecurityContextHolder` pour lire le JWT depuis le SecurityContext

Différences :
- `AppUser` au lieu de `OrganizationUser` (pas de contexte organisation)
- Roles lus depuis `realm_access.roles` au lieu d'un namespace Auth0
- `AccountManager` utilise directement le `sub` du JWT (pas d'appel à une management API externe)
- `KeycloakProperties` au lieu de `Auth0ManagementApiProperties` (juste issuer URI + realm)
