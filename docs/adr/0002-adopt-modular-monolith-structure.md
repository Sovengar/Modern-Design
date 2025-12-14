# 2. Adopt Modular Monolith Structure

Date: 2025-12-14

## Status

Accepted

## Context

The application is growing and requires a clear structure to manage complexity. A traditional layered architecture can lead to tight coupling and poor domain alignment. We want to ensure that features are encapsulated and that the codebase remains maintainable as it scales.

## Decision

We have decided to adopt a **Modular Monolith** architecture. The application is structured into distinct modules based on business domains/features.

The identified modules are:
- `auth`: Handles authentication and authorization.
- `banking`: Core banking domain logic.
- `notification`: Handles system notifications.
- `search`: Provides search capabilities.
- `amazon`: Integration with Amazon services.
- `_shared`: Shared kernel containing common utilities and domain-agnostic code.
- `_config`: Global application configuration.

## Consequences

### Positive
- **High Cohesion**: Code related to a specific feature stays together.
- **Loose Coupling**: Modules interact through well-defined interfaces (ideally).
- **Easier Refactoring**: It is easier to extract a module into a microservice later if needed.

### Negative
- **Complexity**: Requires discipline to prevent cross-module pollution and cyclic dependencies.
- **Shared Kernel**: Changes in the shared kernel can affect multiple modules.
