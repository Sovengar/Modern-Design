Project to show modern design practices.

# Important

Accounting module is a full clean architecture, while user module is a pragmatic one.
I.e. Spring Data JPA is inside the domain and the entity DB and domain are fused.

# Architecture

## Clean Architecture

Domain centric application
Adapter pattern for details (DB, Presenter view, external APIs, third party libraries)
Application layer with use cases for orchestrating domainServices, logging and validation
Avoiding pollution of domain with framework annotations (partially, pragmatically)
*ORM is allowed since it doesn't pollute very much
Relaxed, calls to repository directly are allowed if no business logic is still present
Evolutionary, the code is in the facade and extracted to domainServices when it grows
*Sometimes overengineering just to see how the endgame will look and to see more design patterns

## DDD

Bounded contexts to assemble modules
Aggregates inside the modules
Value Objects for domain objects
Micro types for ids
Domain centric application
Ubiquitous language: create -> register, add -> transfer
Repositories instead of DAOs

## Modules

Facading use cases.
Not allowing internal calls between modules, only through facade.
Preserving FK instead of object in the entity between modules.
Not fully implemented, DB is still shared between modules, only access is restricted.

Not all classes are private because we are packaging, we use ArchUnit and Spring Modulith to compensate language deficiences.

# Testing

- Stubs
- Fakes
- Mocks
- TDD
- BDD
- Approval testing
- Display Beautiful methods
- Nested classes

# Patterns

- SOLID
- Humble Object pattern
- Facade pattern (Grouping use cases, one step closer to modules and microservices)
- Adapter / Anti corruption layer for external services/APIs
- Criteria / Specification

## CQRS

Search class that calls directly the repository and has his own dtos for projections
Zero business logic involved, Zero mutation in the search class
Commands in the normal facade.

## Instantiation

- Factory Classes (Configuration)
- Factory method / Named Constructors
- Builder (for testing or criteria objects)
- ObjectMother (for testing small objects with few combinations)

## Java

- Optional
- Stream
- Default methods
- Records (Immutable DTOs for many different scenarios)
- Sealed? Switch matchers?

## Others

- DTOs (to transfer data between layers)
- Resources (DTOs that expose our API to the external clients, has to be robust for less versioning)
- Value Objects
- Micro Types (AccountId, reserve for PK only)
- CQS separating commands from queries, query after command.
- Internal classes (exception classes,records, micro types, ...)
- Wrapper classes (For external services to map his json to our POJOs)
- Tell Don't Ask (Have getters for mapper but not setters to avoid mutable objects and business logic leaking)
- UUIDs on error logs
- UUIDs set from the client

# Configuration

## Docker

It gets executed automatically with a DB inside a container linked in Spring thanks to the Spring-Compose dependency.

# Testing

Tanto el desarrollo como los tests se hacen con test Containers.
Se utiliza testContainers Desktop para mapear el puerto al 5432 pero no es necesario.

Los tests tienen @Tag("integration") para ejecutar unicamente los tests unitarios.
Para ello editamos un runConfiguration en IntelliJ y le pasamos el tag "!integration"

## Pit

Pit es un framework de testing para java que permite hacer pruebas de mutaciones.

```bash
./mvnw pitest:mutationCoverage
```

Lastimosamente no funciona porque no existe pit para Java 21 todavia.
