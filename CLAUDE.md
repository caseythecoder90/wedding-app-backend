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
- **ALWAYS use utility classes**: Objects, BooleanUtils, StringUtils, IOUtils, CollectionUtils, etc.
- **ALWAYS use constants**: Add string literals (except log statements) to WeddingServiceConstants
- **REST API controllers**: Always create custom annotations for controller documentation to reduce clutter while maintaining professional documentation

## System Architecture Summary

### Core Entities
- **GuestEntity**: Primary invitees with contact info, can be part of family groups, has invitation codes
- **FamilyGroupEntity**: Groups of guests with a primary contact, max attendee limits
- **FamilyMemberEntity**: Additional family members within a group (separate from guests)
- **RSVPEntity**: One-to-one with guests, tracks attendance and plus-ones
- **InvitationCodeEntity**: Unique codes for guest authentication

### RSVP Flow
1. Guest enters invitation code in UI
2. Backend validates code and returns invitation data (single guest, guest+plus-one, or family group)
3. Guest submits RSVP with attendance info
4. System processes and stores RSVP data

### Known Issues/Inconsistencies
- Plus-ones stored as strings in RSVP rather than entities
- Dual person representation (GuestEntity vs FamilyMemberEntity)
- API responses have both familyMembers and additionalGuests fields
- RSVP request handling differs for plus-ones vs family members

### Guest Types Supported
- SOLO: Single guest, no plus-one
- SOLO_WITH_PLUS_ONE: Single guest with plus-one allowed  
- FAMILY_PRIMARY: Primary contact for family group with multiple members
- For REST API controllers, always create custom annotations for the controllers documentation. This helps reduce clutter in the code while maintaining professional documentation