[# AGENTS.md

## Git Rules

**NEVER run `git add` or `git commit` or `git push`.**

The developer must always perform these operations manually.

---

## Build & Test

Common Gradle commands:

```
./gradlew build                          # build all modules
./gradlew test                           # run all tests
./gradlew :msp-<module>:test             # test a single module
./gradlew :msp-<module>:build            # build a single module
./gradlew check                          # build + test + jacoco + sonar inputs
```

**Test framework:** JUnit 5 (`useJUnitPlatform()`). JaCoCo HTML/XML reports are produced automatically after test runs.

---

## Module Map

The platform is organized as a multi-module Gradle project. Each module owns a specific concern:

| Module | Responsibility |
|---|---|
| `msp-application-services-starter` | Service layer starter with Spring Context and transaction support for service-layer beans. |
| `msp-bom` | Aggregator BOM re-exported to consumers. Add new `msp-*` modules as constraints here. |
| `msp-crypto` | AES-256 encryption, SHA-256 hashing, Base64 utilities. |
| `msp-dependencies-bom` | Root BOM; pins all third-party versions via `spring-boot-dependencies:4.0.6`. **Never add code here.** |
| `msp-domain-jpa-starter` | JPA/Hibernate with multi-tenant `RoutableDataSource`, Envers audit, custom PostgreSQL dialect. |
| `msp-domain-jpa-test-starter` | JPA integration test support with embedded PostgreSQL. |
| `msp-jackson` | Jackson `ObjectMapperFactory` singleton and `JSON` utility class. |
| `msp-logging` | Structured logging (`SemanticLogger`, `LoggingContext`, Logback/JSON output). |
| `msp-rest-api-starter` | Spring MVC REST starter: web and Springdoc OpenAPI UI. |
| `msp-rest-api-test-starter` | REST API test starter; composes `msp-rest-api-starter` and `msp-spring-test-starter` for controller-layer tests. |
| `msp-spring-boot-application` | Spring Boot bootstrap: UTC timezone, startup logging, auto-configuration, OpenAPI, Prometheus metrics. |
| `msp-spring-boot-gradle-plugin` | Bundles `spring-boot-gradle-plugin` and `spring-boot-devtools`. |
| `msp-spring-security-starter` | Spring Security starter (core, OAuth2). |
| `msp-spring-security-test-starter` | Security test starter; adds `spring-security-test` for secured-endpoint testing. |
| `msp-spring-test-starter` | Spring-aware test support (boot-test, security-test, kotest-extensions-spring). |
| `msp-test-starter` | Base test library: JUnit 5, Mockito, AssertJ, Kotest, Google Truth. |
| `msp-types` | Core utility types and libraries (Apache Commons, jOOR reflection, Moneta money, JSR-354). No Spring dependency. |
| `msp-util` | General-purpose utilities (Apache Commons, jOOR, Moneta money). |

---

## Dependency Management Rules

- **Never declare version numbers in module `build.gradle.kts` files.** All versions are pinned in `msp-dependencies-bom`.
- When adding a new third-party library, add the version constraint to `msp-dependencies-bom/build.gradle.kts` first, then reference it by name only in the consuming module.
- When adding a new `msp-*` module, add a constraint for it in `msp-bom/build.gradle.kts`.
- `commons-logging` is globally excluded from all configurations — do not add it.

---

## Coding Conventions

### Language
- All implementation is Java 21. Do not add Kotlin source files.
- Gradle build scripts are Kotlin DSL (`build.gradle.kts`, `settings.gradle.kts`).

### Package Naming
Use `com.stano.<module_domain>` where compound names use underscores, not dots.

Examples:
- `com.stano.spring_boot_application`
- `com.stano.domain_jpa`
- `com.stano.crypto`

### Code Style
Enforced by `.editorconfig`:
- 2-space indentation
- LF line endings, UTF-8 encoding
- No trailing whitespace, final newline required

### Design Patterns in Use
- **Factory pattern** for service instantiation (`ObjectMapperFactory`, `TextEncryptionServicesFactory`, `PasswordFactory`)
- **Abstract base classes** for type hierarchies (`AbstractEncryptedText`, `AbstractEncryptedBytes`, `AbstractPassword`)
- **`@AutoConfiguration` + `@Configuration`** for Spring Boot auto-configuration classes
- **Multi-tenancy via `ThreadLocal`:** `RoutableDataSource` and `DatabaseContextHolder` for dynamic datasource switching

### Test Conventions
- **Class naming:** `<Subject>Test` (e.g., `JSONTest`, `AES256EncryptedBytesTest`, `DatabaseContextHolderTest`)
- **Method naming:** Long descriptive camelCase sentences (e.g., `convertingAnObjectToAJsonStringAndThenBackToAnObjectShouldWork`)
- **Assertions:** Primary library is AssertJ (`assertThat(...).isEqualTo(...)`)
- **Test frameworks:** JUnit 5 + Mockito are default
]()
