# spring-error-pattern

A reusable Spring Boot library that provides a standardized way to handle exceptions, format error responses, and manage global error handling across APIs.

## Features

- Global Exception Handler
- Standardized API error responses
- Error catalog support
- Dynamic exception resolver
- Internationalization support
- Custom business exceptions
- Trace ID support
- Logging integration

---

## Architecture

Controller
↓
Service
↓
Throws Exception
↓
GlobalExceptionHandler
↓
Standard Error Response

---

## Installation

```xml
<dependency>
    <groupId>com.yourcompany</groupId>
    <artifactId>spring-error-pattern</artifactId>
</dependency>
```

---

## Example

{
    "timestamp":"2026-05-16T15:00:00",
    "code":"USR001",
    "message":"User not found",
    "traceId":"ABC123"
}
