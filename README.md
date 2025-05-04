# Resume

Project to show modern design practices. DDD, SOLID, CQRS, clean architecture, Modules and many more.

# Configuration, Execution & Deploy

## Configuration

- Clone the project from GitHub.
- Select JDK 21 in your IDE project folder.
- Select the correct Maven Route (Maven Wrapper) and .mvn/settings.xml if you have one in your IDE.
- **[Optional]** Configure Sonar in your IDE.
- Download or Enable Lombok Plugin in your IDE.
- Get a local.env (you can inspire from example.env).
- Edit the runConfiguration in IntelliJ and establish y the tag execution "!integration" for unit tests.
- Run maven with `compile` goal to assert everything is ok.
- Select the QClasses from your target folder in your IDE scanner classes.

Check the dependencies section for more detailed info.

## Execution

**[Automatic with Spring Compose]** Start Dependencies in a container and application in your local machine with the Play Button on your
IDE or in your terminal.

- Establish the env vars that Spring needs as launch parameters.

**[Manual]** Execute the container from your terminal or GUI. Execute your app from your terminal or IDE.

**[Manual with all in containers]** Execute a compose.yml that executes a Dockerfile.local which has all env vars needed.

- In our application-local.properties we should disable Spring Compose `spring.docker.compose.enabled=false`

---

You can execute your app from the Play/Debug Button in your IDE or via commands from your terminal:

```bash
./mvnw spring-boot:run ACTIVE_PROFILE=local
```

You can execute the container from the GUI or from your terminal with: `docker-compose -f yourCompose.yml up`.

- **yourCompose** could be either compose.local.spring.yml or compose.local.yml, depending on your needs.

## Cloud

In Cloud, we don't use Docker Compose, instead the Dockerfile with Jenkins:

- **Dockerfile:** We define the base image of the project.
- **run.sh** We define the commands to start the application in Cloud.
- **Jenkins:** TODO...

# Architecture

### Clean Architecture

Domain-centric application
Adapter pattern for details (DB, Presenter view, external APIs, third party libraries).
Application layer with use cases for orchestrating domainServices, logging and validation.
Avoiding pollution of domain with framework annotations (partially, pragmatically).
*ORM is allowed since it doesn't pollute very much.
Relaxed, calls to repository directly are allowed if no business logic is still present.
Evolutionary, the code is in the facade and extracted to domainServices when it grows.
*Sometimes overengineering just to see how the endgame will look and to see more design patterns.

### DDD

Bounded contexts to assemble in modules
Aggregates inside the modules
Value Objects for domain objects
Micro types for ids
Domain centric application
Ubiquitous language: create → register, add → transfer
Repositories instead of DAOs

### Modules

Facading use cases.
Not allowing internal calls between modules, only through interface API.
Preserving FK instead of an object in the entity between modules.
Not fully implemented, DB is still shared between modules, only access is restricted.

Not all classes are private because we are packaging, we use ArchUnit and Spring Modulith to compensate for language deficiencies.

### Testing

- Stubs
- Fakes
- Mocks
- TDD
- BDD
- Approval testing
- Display Beautiful methods
- Nested classes

### Patterns

- SOLID
- Humble Object pattern
- Facade pattern (Grouping use cases, one step closer to modules and microservices)
- Adapter / Anti-corruption layer for external services/APIs
- Criteria / Specification

### CQRS

Search class that calls directly the repository and has his own dtos for projections
Zero business logic involved, Zero mutation in the search class
Commands in the normal facade.

### Instantiation

- Factory Classes (Configuration)
- Factory method / Named Constructors
- Builder (for testing or criteria objects)
- ObjectMother (for testing small objects with few combinations)

### Java

- Optional
- Stream
- Default methods
- Records (Immutable DTOs for many different scenarios)
- Sealed? Switch matchers?

### Others

- DTOs (to transfer data between layers)
- entityDto (DTOs that expose our API to the external clients have to be robust for less versioning)
- Value Objects
- Micro Types (AccountId, reserve for PK only)
- CQS separating commands from queries, query after command.
- Internal classes (exception classes, records, micro types, ...)
- Wrapper classes (For external services to map his JSON to our POJOs)
- Tell Don't Ask (Have getters for mapper but not setters to avoid mutable objects and business logic leaking)
- UUIDs on error logs
- UUIDs set from the client

# Dependencies

## JDK

- **Download** from https://adoptium.net/es/temurin/releases/ or use **SDKMAN** https://sdkman.io/
- Select JDK 21 in your IDE project folder.

## Env files `.env`

`.env` files are used to manage environment variables locally.

- **`.env` file:** Contains common configurations for local development.
- **file:`local.env`** Contains specific configurations for local development, should not be uploaded to the repository.
- **file:`example.env`** Contains an example for local execution.

The **ACTIVE_PROFILE** variable must always be local, what changes are the rest of the variables.

It is important not to put sensitive variables in `.env`, only those for the Docker Compose interpreter, as this file is uploaded to the
repository.

Sensitive information will be stored in the `local.env`, `dev.env`, `pre.env`, `pro.env` files. Consult with the development team to obtain
them.

You can copy `example.env` to `local.env` as a base for your local environment.

## BD and FlyWay

Using PostgresSQL 16.

With FlyWay we ensure that migrations are applied and versioned.
FlyWay is configured programmatically to only function in local because we don't have permissions to change the BD from the backend in
Cloud.

Still, we achieve this benefit:

- H2-Like behavior.
- Execute and validate production-like scripts in your local environment first (ALTER TABLE vs. add field to the schema when it is h2-like).
- Having an historic of scripts applied and ordered by the timestamps.
- If we want to modify the data that populates the BD, we should modify the file resources/db/dev/R__populate_db.sql
- If we want to modify the tables or making inserts to master tables, we should create a new script in resources/db/migrations
  *Posteriorly this should be uploaded in parallel to Cloud.

In ``` application-local.properties ``` we have the directive ``` database.clean=true ``` to achieve an H2-like behavior.

## Maven

This project uses **Maven Wrapper**, that means it is not necessary to download Maven manually.

Make sure that your IDE has in his Maven Home path "Use Maven Wrapper" and the user settings file is ./.mvn/settings.xml if you have one.

If you need to execute a Maven command, you can do it from your IDE or via terminal with `mvnw` (for Unix Systems) y `mvnw.cmd` (for
Windows)

Example:

```bash
./mvnw clean install # Unix
```

```bash
mvnw.cmd clean install # Windows
```

This guarantees that every developer uses the same maven version, avoiding inconsistencies between environments.

## Container tools

You can use PODMAN or Docker via UNIX or WSL2 or Docker Desktop via Windows.
https://docs.docker.com/desktop/features/wsl/

## Testing

The development and tests are made with test Containers.
TestContainers can be improved with TestContainer Desktop to map explicitly to port 5432.

Tests with @Tag("integration") to execute exclusively unit tests.
We need to edit the runConfiguration in IntelliJ and establish y the tag execution "!integration"

## QueryDSL

Is a database library to make queries with our java models instead of plain fragile SQL strings.
Generates Qclasses from or @Entities with the apt-maven-plugin when we compile with Maven.
The Qclasses are generated on the target folder, the IDE should be configured to scan that folder.

Doesn't work with Java 21 as of today.

## Sonar

Download in your IDE the Sonar plugin and configure a Maven goal to link with your organization Sonar if needed:

```bash
sonar:sonar -Dsonar.projectKey=CTTIGEN-SAFCATBACK -Dsonar.host.url=https://yourCompany/sonarqubece -Dsonar.login=yourLoginToken -Dsonar.branch.name=develop -f pom.xml
```

Replace **yourLoginToken** with the token provided by your company and replace **yourCompany** with the url to their Sonar.

...

## Others

### IDE

This app doesn't need env vars to be set as launch parameters.
Start the app with the play button or execute in your terminal with

```bash
./mvnw spring-boot:run ACTIVE_PROFILE=local
```

### EditorConfig

With .editorconfig we standarize the syntaxis of the code with all the team.

### Collections with Bruno

In the collections folder we have Brunoized the requests versioned with Git and all the advantages that this brings.

### Lombok

This project uses Lombok, you will need an IDE that has a plugin that allows Lombok generation code.

### ArchUnit

### SpringModulith

### Spring profiles

By default, the application.properties file is executed, because there is no env var with the profile, the application-local will be
executed.

### Spring Compose

With Spring Compose dependency, the life cycle of your containers defined in your compose.yml is delegated to Spring, in other words, you
can specify from Spring when the containers are started, restarted or stopped automatically.

Furthermore, it is not necessary to specify things like spring.jdbc.url because Spring will pick up from the container itself.

Nonetheless, we should consider that the backend will be in local (not in a container), so the env vars that we need for our application
will be needed in your launcher parameters.

### Pit

Is a framework that makes mutations to the bytecode to validate if our tests are robust.

```bash
./mvnw pitest:mutationCoverage
```

### MapStruct

Deleted, but it generates Impl classes with a Maven plugin, execute Maven if you have problems with it.
