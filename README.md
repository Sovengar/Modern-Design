Proyecto para mostrar practicas de diseño moderno.

# Important

Account es una version full clean architecture, mientras que User es una mas pragmatica.
Por ejemplo, SpringJPA esta en el domain y el entity de db y del domain estan fusionados.

En el facade, no todos los usecases tienen su propia clase, esto es debido a que la idea es extraerlo cuando crece, no by default.

# Architecture

- Clean architecture
- SOLID
- DDD

# Testing

- Stubs
- Fakes
- Mocks
- TDD
- BDD

# Patterns

- Humble Object pattern
- Facade pattern (Grouping usecases, one step closer to modules and microservices)
- CQRS (soft implementation)
- Adapter / Anti corruption layer for external services/APIs

## Instantiation

- Factory Classes (Configuration)
- Factory method / Named Constructors
- Builder (for mapper, testing and objects with many attributes)
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
- CQR separating commands from queries, query after command.
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
