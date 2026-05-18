# spring-error-pattern

> **V1** — versão inicial, ainda não lançada (`0.0.1-SNAPSHOT`)

A reusable Spring Boot library that provides a standardized way to handle exceptions, format error responses, and manage global error handling across APIs.

## Overview

Part of the **MDS Shared Core** ecosystem (`com.mds`). This library centralizes exception handling so every microservice returns consistent, structured error responses.

---

## Tech Stack

| Dependency | Version |
|---|---|
| **Java** | 21 (LTS) |
| **Spring Boot** | 4.0.6 |
| **Jackson** | 3.x (`tools.jackson.*`) |
| **Lombok** | managed by Spring Boot |

---

## Features

- **Global Exception Handler** — `@RestControllerAdvice` that catches all exceptions
- **Typed Exceptions** (unchecked) — `ProductException`, `TechnicalException`, `AuthorizationException`, `ValidationException`, `TransactionalException`
- **Exception Resolver SPI** — pluggable `ExceptionResolver<T>` interface for custom exception types
- **Error Response Model** — structured JSON with code, type, title, message, action, status, and date
- **Error Builder Customizer** — `ErrorBuilderCustomizer` functional interface for response enrichment
- **Utility Classes** — `ErrorUtils` for error generation/conversion, `ThrowUtils` for guard clauses
- **i18n Support** — multi-language error messages via `MessageSource` (en, pt-BR, es)
- **Logging Integration** — automatic error logging with SLF4J (level-aware: `warn` for VALIDATION/PRODUCT, `error` for TECHNICAL/SECURITY/TRANSACTION)
- **Dispatch-based Exception Factory** — `ErrorUtils` uses centralized dispatch methods to eliminate duplicated switch blocks across 9 `throwError()` overloads

---

## Architecture

```text
Controller
     ↓
  Service
     ↓
Throws Exception (unchecked — RuntimeException)
     ↓
ErrorExceptionHandler (@RestControllerAdvice)
     ↓
ExceptionResolver<T> (pluggable)
     ↓
ErrorResponse (JSON, locale-aware)
```

### Exception Hierarchy

```text
RuntimeException
  └── BaseException (code, status)
        ├── GeneralException (title, detail, action, httpStatusCode)
        │     ├── ProductException
        │     ├── TechnicalException
        │     ├── AuthorizationException
        │     ├── ValidationException
        │     └── TransactionalException
        └── ErrorResponse.DeserializationException
```

---

## Installation

```xml
<dependency>
    <groupId>com.mds</groupId>
    <artifactId>spring-error-pattern</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

> **Requires Java 21+** and **Spring Boot 4.0.x**.

---

## Project Structure

```text
com.mds.error.handler
├── config/                    — Jackson config, MessageSource config, ErrorMessages resolver
├── enumerator/                — Action, Type enums
├── exception/
│   ├── base/                  — BaseException (RuntimeException)
│   ├── customizer/            — ErrorBuilderCustomizer
│   ├── handler/               — ErrorExceptionHandler
│   ├── helper/                — ErrorExceptionHandlerHelper
│   ├── keys/                  — ErrorStatusKeys, ExceptionMessageKeys (i18n keys)
│   └── resolver/              — ExceptionResolver<T> + implementations
├── interfaces/                — ExecutableErrorHandler
├── model/
│   ├── general/               — Error model
│   └── response/              — ErrorResponse + DeserializationException
└── utils/                     — ErrorUtils, ThrowUtils, CacheControlUtils

src/main/resources/
└── messages/
    ├── error-handler-messages.properties        — English (default)
    ├── error-handler-messages_pt_BR.properties   — Portuguese (Brazil)
    └── error-handler-messages_es.properties      — Spanish
```

---

## Error Response Example

```json
{
  "error": {
    "code": "EHL_0000",
    "type": "PRODUCT",
    "title": "An error occurred!",
    "message": "User not found",
    "status": 422,
    "action": "RETRY_ON_STATE",
    "date": "2026-05-16T15:00:00.000Z"
  }
}
```

## i18n — Multi-language Support

Error messages are resolved at runtime based on the `Accept-Language` HTTP header via Spring's `MessageSource`.

**Adding a new locale:** create `messages/error-handler-messages_XX.properties` on the classpath.

**Overriding messages:** place a file with the same basename on your application's classpath — standard `ResourceBundleMessageSource` behaviour.

---

## Changelog (V1)

### Refactoring & Improvements

- **PA1 — `throwError()` dispatch consolidation:** 9 overloads in `ErrorUtils` now delegate to 8 private `newException*()` dispatch methods, each with a single `switch`. Zero duplicated logic.
- **PA2 — `ErrorResponse(String)` constructor:** changed from `Object` to `String` to eliminate ambiguity with the Lombok `@AllArgsConstructor(Error)`. Non-string payloads are handled via `ErrorUtils.toJsonString()` with a warning log.
- **PA3 — Level-aware logging in `GeneralException.logInformation()`:** `log.warn` for `ValidationException` / `ProductException` (expected business/input errors), `log.error` for `TechnicalException` / `AuthorizationException` / `TransactionalException` (unexpected/infrastructure errors).
- **Jackson 3 migration:** all `com.fasterxml.jackson.core.*` / `com.fasterxml.jackson.databind.*` imports migrated to `tools.jackson.*`. `ObjectMapper` creation uses the builder pattern. `JavaTimeModule` auto-detected (no manual registration).
- **Java 21 + Spring Boot 4.0.6:** upgraded from Java 17 / Spring Boot 3.4.0.
- **Javadoc corrections:** "Checked exception" → "Unchecked exception" in all typed exception classes.

---

## Author

Martins Desenvolvimento de Sistemas (MDS)
