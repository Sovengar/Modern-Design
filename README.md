Proyecto para mostrar practicas de diseño moderno.

Clean architecture
SOLID
TDD
BDD
DDD

Patterns
Humble Object pattern
Adapter Pattern
Facade pattern
Command pattern

Factory Classes
Factory method
Builder
ObjectMother

Testing
Stubs
Fakes
Mocks

# Development

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
