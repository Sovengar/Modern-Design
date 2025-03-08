Proyecto para mostrar practicas de diseño moderno.

# Important

Account es una version full clean architecture, mientras que User es una mas pragmatica.
Por ejemplo, SpringJPA esta en el domain y el entity de db y del domain estan fusionados.

# Architecture

## Clean Architecture

Domain centric application
Adapter pattern for details (DB, Presenter view, external APIs, third party libraries)
Application layer with usecases for orquestrating domainServices, logging and validation
Avoiding pollution of domain with framework annotations (partially, pragmatically)
*ORM is allowed since it doesnt pollute very much
Relaxed, calls to repository directly are allowed if no business logic is still present
Evolutionary, the code is in the facade and extracted to domainServices when it grows
*Sometimes overengineering just to see how the endgame will look and to see more design patterns

## DDD

Bounded contexts to assemble modules
Aggregates inside the modules
Value Objects for domain objects
Microtypes for ids
Domain centric application
Ubiquitous language: create -> register, add -> transfer
Repositories instead of DAOs

## Modules

Facading usecases.
Not allowing internal calls between modules, only through facade.
Preserving FK instead of object in the entity between modules.

# Testing

- Stubs
- Fakes
- Mocks
- TDD
- BDD
- Approval testing
- Display Beatufil methods
- Nested classes

# Patterns

- SOLID
- Humble Object pattern
- Facade pattern (Grouping usecases, one step closer to modules and microservices)
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

# Configuration

## Docker

Se ejecuta automaticamente la BD dentro de un container vinculado a Spring gracias a la dependencia Spring-Compose.

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
