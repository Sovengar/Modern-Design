# 5. Shared Kernel Module (_shared)

Date: 2025-12-14

## Status

Accepted

## Context

Multiple modules require common functionality such as utility classes, base domain objects, or generic infrastructure wrappers. Duplicating this code leads to inconsistency and maintenance burden.

## Decision

We have created a `_shared` module (Shared Kernel) to house common code.

Responsibilities:
- Common value objects (e.g., Money, Email).
- Base repository/service interfaces.
- Cross-cutting concerns like logging or exception handling utilities.

## Consequences

### Positive
- Reduces code duplication.
- Enforces consistency across the application.

### Negative
- Can become a "junk drawer" if not strictly managed.
- Coupling: Changes here can ripple through all other modules. Dependencies on `_shared` must be carefully monitored.
