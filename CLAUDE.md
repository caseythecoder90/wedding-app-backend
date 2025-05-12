# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build/Run Commands
- Build: `mvn clean install`
- Run locally: `mvn spring-boot:run -Dspring-boot.run.profiles=local`
- Run specific test: `mvn test -Dtest=TestClassName#methodName`
- Run all tests: `mvn test`
- Start PostgreSQL: `docker-compose -f src/main/resources/docker/docker-compose.yml up -d`

## Code Style Guidelines
- Use Java 17 language features including text blocks and patterns
- Import organization: Alphabetical order with static imports last
- Use Lombok annotations (@Slf4j, @Data, @RequiredArgsConstructor)
- Follow Spring Boot conventions for service/repository/controller layers
- Use builder pattern for complex object construction
- Exception handling: Use custom WeddingAppException with error keys
- Logging: Use SLF4J (log.info) with BEGIN/END markers
- Naming: CamelCase for variables, PascalCase for classes
- Document APIs with Swagger annotations (@Tag, custom api doc annotations)