# Der Die Das

![Build](https://github.com/dufernandes/derdiedas/actions/workflows/maven.yml/badge.svg?branch=master)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
![Java](https://img.shields.io/badge/Java-8-blue?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.1-brightgreen?logo=springboot)
![JWT](https://img.shields.io/badge/Auth-JWT-orange)
![Docker](https://img.shields.io/badge/Docker-ready-blue?logo=docker)
![H2](https://img.shields.io/badge/DB-H2%20%7C%20PostgreSQL%20%7C%20CockroachDB-lightgrey)

> *Der, die, or das?* — A REST API backend for learning German grammatical articles, built with Spring Boot, JWT authentication, and test-driven API documentation.

---

## What It Does

German has three grammatical articles — *der* (masculine), *die* (feminine), and *das* (neuter) — and learning which article belongs to which noun is one of the hardest parts of the language. There are patterns, but also thousands of exceptions.

This project is the backend for a flashcard-style learning platform. It manages users, a vocabulary database of ~3,000 German nouns (each with their article and English translation), and a study flow that assigns words in batches and tracks which ones a user has learned.

The core loop:

1. Register → `POST /users`
2. Authenticate → `POST /login` (returns a JWT)
3. Get your first batch of words → `PUT /users/{id}?action=assignLearningWords`
4. Mark a word as learned → `PUT /learningWords/{id}?isStudied=true`
5. When you finish a batch, get the next one — repeat until done

---

## Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 2.1 |
| Security | Spring Security + JWT (stateless) |
| Persistence | Spring Data JPA / Hibernate |
| Databases | H2 (default), PostgreSQL, CockroachDB |
| Documentation | Spring REST Docs (test-driven, generated from integration tests) |
| Testing | JUnit 5, Mockito, Spring Security Test, Jacoco |
| Build | Maven |
| Container | Docker (multi-stage build) |
| CI | GitHub Actions |

---

## Quickstart with Docker

The fastest way to run the application locally — no Java or Maven required:

```bash
docker build --tag derdiedas .
docker container run -p 8080:8080 derdiedas
```

The app starts on `http://localhost:8080` with an in-memory H2 database, pre-loaded with ~3,000 German words and 10 sample users.

---

## Quickstart with Maven

```bash
# Clone
git clone https://github.com/dufernandes/derdiedas.git
cd derdiedas

# Build, run all tests, generate API docs
mvn clean verify package

# Start on port 8080
mvn spring-boot:run
```

---

## API Walkthrough

All endpoints except registration require a JWT in the `Authorization: Bearer <token>` header.

### 1. Register a user

```bash
curl -i -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"email":"you@example.com","password":"secret","firstName":"Ada","lastName":"Lovelace"}'
```

### 2. Authenticate

```bash
curl -i -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"username":"you@example.com","password":"secret"}'
```

The response headers contain:
- `Authorization: Bearer <token>` — use this in all subsequent requests
- `userId: <id>` — your user ID

### 3. Assign a batch of words to study

```bash
curl -i -X PUT "http://localhost:8080/users/{userId}?action=assignLearningWords" \
  -H "Authorization: Bearer <token>"
```

Returns a `UserDto` with a `wordsStudying` array — each entry has a `learningWordId`, the word, its article, and its English translation.

### 4. Mark a word as learned

```bash
curl -i -X PUT "http://localhost:8080/learningWords/{learningWordId}?isStudied=true" \
  -H "Authorization: Bearer <token>"
```

### 5. Get the next batch

Once all words in a batch are marked as studied, calling `assignLearningWords` again automatically advances to the next group.

---

## API Documentation

Documentation is generated automatically from integration tests using **Spring REST Docs** — meaning the docs are always in sync with the actual API behaviour.

Generate and serve it:

```bash
mvn clean verify package
mvn spring-boot:run
```

Then open: [http://localhost:8080/docs/api/index.html](http://localhost:8080/docs/api/index.html)

---

## Architecture

```
com.derdiedas/
│
├── authentication/        # JWT filter chain, Spring Security config, BCrypt
│   ├── JWTAuthenticationFilter   # Handles POST /login, issues JWT
│   ├── JWTAuthorizationFilter    # Validates JWT on every request
│   └── WebSecurity               # Security config (disabled via nosecure profile)
│
├── model/                 # JPA entities
│   ├── Word               # A German noun: article + word + translation
│   ├── LearningWord       # Word + study status (studied: true/false) per user
│   ├── User               # Implements UserDetails; owns a set of LearningWords
│   └── DefaultSettings    # Configurable words-per-batch setting
│
├── service/               # Business logic
│   ├── UserService        # Registration, auth integration, word batch assignment
│   ├── LearningWordService# Updates study status
│   ├── WordService        # Word lookup
│   └── DefaultSettingsService
│
├── controller/            # REST layer
│   ├── UserController     # /users
│   └── LearningWordController  # /learningWords
│
├── dto/                   # API request/response shapes (ModelMapper)
├── repository/            # Spring Data JPA repositories
├── bootstrap/             # Startup data loader + word importers
└── config/                # Caching, global exception handlers
```

**Key design decisions:**

- `User` implements Spring Security's `UserDetails` directly — no separate identity entity, keeping the model clean and the auth integration tight.
- `LearningWord` is a join entity between `User` and `Word` that carries state (studied/not studied). This avoids a plain many-to-many and makes progress tracking explicit in the schema.
- Word batch assignment is handled entirely in `UserService` using JPA pagination — no custom SQL, fully portable across databases.
- Security is toggled via Spring profiles (`nosecure`) rather than environment variables, making local development and H2 console access frictionless.
- Spring REST Docs generates API documentation from actual HTTP request/response pairs captured during integration tests — not from annotations or manually maintained files.

---

## Running Tests

```bash
# All tests (unit + integration) + coverage report + API docs
mvn clean verify package

# Unit tests only
mvn verify -Dit.skip=true
```

Coverage report: `target/site/jacoco/index.html`

---

## Database Profiles

| Profile | Command | Database |
|---|---|---|
| Default (H2) | `mvn spring-boot:run` | In-memory, reset on restart |
| PostgreSQL | `mvn spring-boot:run -Dspring-boot.run.profiles=localdb` | Local Postgres |
| CockroachDB | `mvn spring-boot:run -Dspring-boot.run.profiles=localcockroachdb` | Local CockroachDB |
| No security | `mvn spring-boot:run -Dspring-boot.run.profiles=nosecure` | H2 + console enabled |

Connection strings live in `src/main/resources/application-{profile}.properties`. All queries use JPQL — switching databases requires only a profile change, no SQL rewrites.

### H2 Console (nosecure profile only)

URL: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

| Setting | Value |
|---|---|
| Driver Class | `org.h2.Driver` |
| JDBC URL | `jdbc:h2:mem:testdb` |
| User Name | `sa` |
| Password | *(leave empty)* |

---

## Javadoc

```bash
mvn clean javadoc:javadoc package
mvn spring-boot:run
```

Then open: [http://localhost:8080/docs/apidocs/index.html](http://localhost:8080/docs/apidocs/index.html)

---

## What's Next

- **Upgrade to Spring Boot 3.x / Java 21** — migrate from `javax.*` to `jakarta.*`, adopt virtual threads
- **Refresh token support** — currently JWTs are stateless with no refresh mechanism
- **Pagination on word assignment** — expose configurable batch size per user via the API
- **Frontend** — a React or Vue client to make the learning loop interactive

---

## Author

**Eduardo Fernandes** · Berlin
[github.com/dufernandes](https://github.com/dufernandes)

*This project helped me land my current job. It was built to show how I think about backend architecture — clean layering, test-driven documentation, and zero-friction local development.*

---

## License

[Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0)
