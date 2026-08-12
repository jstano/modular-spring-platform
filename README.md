# Modular Spring Platform

An opinionated Spring Boot platform library that standardizes cross-cutting concerns across microservices: logging, encryption, JSON serialization, JPA with multi-tenancy, REST APIs, security, and testing infrastructure.

## Requirements

- **Java:** 21 (source and target compatibility)
- **Gradle:** 9.6.1 (wrapper included)
- **Spring Boot:** 4.0.6 (managed by BOM)

## Using the Platform

### 1. Configure the Maven Repository

The platform is published to Maven Central, so no extra repository configuration is needed beyond the default:

```kotlin
dependencyResolutionManagement {
  repositories {
    mavenCentral()
  }
}
```

### 2. Import the BOM

Add the aggregator BOM to your `build.gradle.kts`. `msp-bom` pins the version of every `msp-*` module (and transitively pulls in `msp-dependencies-bom` for third-party version pins), so individual `msp-*` dependencies don't need their own version:

```kotlin
dependencies {
  implementation(enforcedPlatform("com.stano:msp-bom:0.2.0"))

  // Now add individual starters (no version needed):
  implementation("com.stano:msp-spring-boot-application")
  implementation("com.stano:msp-rest-api-starter")
  implementation("com.stano:msp-domain-jpa-starter")
  implementation("com.stano:msp-spring-security-starter")

  testImplementation("com.stano:msp-spring-test-starter")
  testImplementation("com.stano:msp-domain-jpa-test-starter")
}
```

### 3. Bootstrap Your Application

Replace `SpringApplication.run()` with `MspSpringApplication.run()`:

```java
@SpringBootApplication
public class MyApplication {
  public static void main(String[] args) {
    MspSpringApplication.run(MyApplication.class, args);
  }
}
```

This initializes logging context, redirects stdout/stderr to SLF4J, sets UTC timezone, and logs startup information.

## Module Reference

| Module | Responsibility | Spring-Free |
|---|---|---|
| **`msp-common`** | Foundational types with no Spring dependency: exception hierarchy, `@Generated` annotation, UUID generation, Apache Commons, jOOR, Moneta money | ✓ |
| **`msp-logging`** | Structured logging with `SemanticLogger`, `LoggingContext`, logback JSON output | ✓ |
| **`msp-crypto`** | AES-256 encryption, SHA-256 hashing, Base64 utilities (text, binary, passwords) | ✓ |
| **`msp-jackson`** | Jackson `ObjectMapperFactory` singleton and `JSON` utility class | ✓ |
| **`msp-data-source-starter`** | Multi-tenant `RoutableDataSource`, `DatabaseContextHolder`/`DatabaseContextRunner`, plain `DataSource` creation |  |
| **`msp-schema-starter`** | Schema/migration bootstrapping (`SchemaManager`, `MigrationAutoConfiguration`) run before JPA starts |  |
| **`msp-application-services-starter`** | Service layer starter with Spring Context and transaction support |  |
| **`msp-spring-boot-application`** | Spring Boot bootstrap (`MspSpringApplication`) with OpenAPI, Prometheus metrics, health/metrics endpoints, global exception handling |  |
| **`msp-rest-api-starter`** | Spring MVC REST starter with Springdoc OpenAPI UI |  |
| **`msp-spring-security-starter`** | Spring Security starter (core, OAuth2) |  |
| **`msp-domain-jpa-starter`** | JPA/Hibernate integration: `@EnableJpa`, `AbstractEntity`/typed ids, routing-aware repositories, encrypted-field converters |  |
| **`msp-spring-boot-gradle-plugin`** | Re-exports spring-boot-gradle-plugin and spring-boot-devtools |  |
| **`msp-test-starter`** | Base test library (JUnit 5, Mockito, AssertJ, Kotest) |  |
| **`msp-spring-test-starter`** | Spring-aware test support |  |
| **`msp-rest-api-test-starter`** | REST controller test starter |  |
| **`msp-spring-security-test-starter`** | Security-layer test support |  |
| **`msp-domain-jpa-test-starter`** | JPA integration tests with embedded PostgreSQL |  |
| **`msp-bom`** / **`msp-dependencies-bom`** | Aggregator BOM (`msp-bom`, re-exports every `msp-*` module version) and root third-party version BOM (`msp-dependencies-bom`) |  |

### Key APIs by Module

#### `msp-spring-boot-application`
- `MspSpringApplication.run(Class<?>, String...)` — Application bootstrap entry point (initializes logging context, redirects stdout/stderr to SLF4J, sets UTC timezone)
- `GlobalExceptionHandler` — Maps `msp-common`'s exception hierarchy (and JPA/reflection/IO exceptions) to RFC 7807 `ProblemDetail` responses
- `DefaultSpringSecurityConfig` — Default `SecurityFilterChain`/CORS configuration
- `ThreadPoolConfig` — Platform `TaskExecutor` thread pool bean
- Health/metrics endpoints are provided by `spring-boot-starter-actuator` (pulled in transitively) and Prometheus/OpenTelemetry integration, not by custom controllers in this module

#### `msp-logging`
- `SemanticLogger.using(logger).with("key", value).info("msg")` — Fluent structured logger
- `LoggingContext.with(key, value).run(Runnable)` — MDC context wrapper
- Automatic JSON output via logstash-logback-encoder

#### `msp-jackson`
- `ObjectMapperFactory.getInstance()` — Singleton `ObjectMapper` with opinionated config
- `ObjectMapperFactory.configure(JsonMapper.Builder)` — Customization hook
- `JSON.parse(String, Class<T>)` — Null-safe deserialization
- `JSON.toString(Object)` — Serialization helper

#### `msp-crypto`
- `TextEncryptionServicesFactory.getInstance()` → `TextEncryptionServices.encryptString()`/`decryptString()`
- `BinaryEncryptionServicesFactory.getInstance()` → `BinaryEncryptionServices.encryptBytes()`/`decryptBytes()`
- `PasswordEncryptionServicesFactory.getInstance()` → `PasswordEncryptionServices.encryptPassword()`/`passwordMatches()`
- `AES256EncryptedText`, `AES256EncryptedBytes`, `SHA256Password` — Strongly-typed cipher-text classes
- `EncryptionSecretProvider.getSecret()` — Read from `msp.encryption.secret` system property

#### `msp-common`
Exception hierarchy for standard error responses (mapped to HTTP status by `GlobalExceptionHandler` in `msp-spring-boot-application`):
- `BadRequestException`, `InvalidRequestException` — HTTP 400
- `UnauthorizedException` — HTTP 401
- `ForbiddenException` — HTTP 403
- `ResourceNotFoundException` — HTTP 404
- `ResourceConflictException` — HTTP 409
- `ResourceLockedException` — HTTP 423
- `InternalServerError` — HTTP 500
- `ServiceUnavailableException` — HTTP 503

Also: `UUIDGenerator` — UUID generation utility; `@Generated` — marks generated code for JaCoCo exclusion.

#### `msp-data-source-starter`
- `DatabaseContextHolder.setDatabaseId(Long)` / `getDatabaseId()` / `clear()` — Multi-tenant context (`ThreadLocal`)
- `DatabaseContextRunner.runWithDatabase(id, supplier)` — Run a task within a tenant context without manual try/finally
- `RoutableDataSource` — `AbstractRoutingDataSource` that routes to a target `DataSource` based on the current context key
- `RoutableDataSourcesLoader<K>` — Interface to implement for loading/refreshing the set of tenant datasources
- `DataSourceFactory.createDataSource(...)` — Builds a HikariCP-backed `DataSource` from bound Spring properties

#### `msp-domain-jpa-starter`
- `@EnableJpa` — Enables JPA repositories, auditing, and platform Hibernate defaults on your `@SpringBootApplication`
- `AbstractEntity<ID>` — Base `@MappedSuperclass` with id, audit timestamps, and optimistic-locking version
- `DatabaseId`, `EntityId` — Typed entity identifier abstractions and their JPA `AttributeConverter`s
- `EntityRepository<T,ID>`, `ReadOnlyRepository<T,ID>` — `@NoRepositoryBean` Spring Data repository interfaces, backed by `RoutingRepositoryFactoryBean` for tenant-aware routing
- `TraceIdStatementInspector` — Hibernate `StatementInspector` that tags SQL statements with the current MDC trace id
- Attribute converters for encrypted/money fields: `EncryptedTextAttributeConverter`, `EncryptedBytesAttributeConverter`, `PasswordAttributeConverter`, `MoneyAttributeConverter`
- `JpaDataSourceAutoConfiguration` — Provides a plain `DataSource` bean unless one is already defined (back off to supply your own `RoutableDataSource` for multi-tenancy — see [Multi-Tenancy](#multi-tenancy))

#### `msp-schema-starter`
- `MigrationAutoConfiguration` — Runs schema migrations before `JpaDataSourceAutoConfiguration` initializes JPA
- `SchemaManager` — Installs/migrates the database schema against a `DataSource`

#### `msp-domain-jpa-test-starter`
- `@JpaTest` — Meta-annotation combining `@DataJpaTest` with the platform's embedded-Postgres test settings
- `BaseJpaTest` — Abstract base test class with an injected `EntityManager`
- `EntityInstancio` — Instancio-based helper for generating test entity instances with sensible defaults

## Configuration

### Encryption

Override the encryption secret (default is insecure and for development only):

```bash
java -Dmsp.encryption.secret=your-secure-secret app.jar
```

The secret is used for AES-256 encryption and should be stored securely in production (e.g., vault, environment variable).

### Logging

Structured logging output is in JSON format via logstash-logback-encoder. Configure via `logback.xml` in `src/main/resources/`:

```xml
<configuration>
  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <encoder class="net.logstash.logback.encoder.LogstashEncoder" />
  </appender>
  <root level="INFO">
    <appender-ref ref="STDOUT" />
  </root>
</configuration>
```

Use `LoggingContext` for MDC (Mapped Diagnostic Context):

```java
LoggingContext.with("request-id", requestId)
              .with("user-id", userId)
              .run(() -> logger.info("Processing request"));
```

### Multi-Tenancy

`msp-domain-jpa-starter` only creates a plain `DataSource` bean when one isn't already defined (`JpaDataSourceAutoConfiguration` is `@ConditionalOnMissingBean(DataSource.class)`). To route JPA/JDBC calls across tenant databases, supply your own `DataSource` bean backed by a `RoutableDataSource`, wired to a `RoutableDataSourcesLoader<Long>` that knows how to discover/build each tenant's datasource:

```java
@Configuration
public class MultiTenantDataSourceConfig {
  @Bean
  public DataSource dataSource(RoutableDataSourcesLoader<Long> loader) {
    return new RoutableDataSource(loader);
  }

  @Bean
  public RoutableDataSourcesLoader<Long> routableDataSourcesLoader(Environment environment) {
    return () -> new RoutableDataSources<>(loadTenantDataSources(environment));
  }
}
```

In your request handler, set the current tenant context before issuing JPA/JDBC calls:

```java
DatabaseContextHolder.setDatabaseId(tenantId);
try {
  // JPA queries now route to the tenant's datasource
  service.doWork();
} finally {
  DatabaseContextHolder.clear();
}
```

Or use the convenience runner:

```java
DatabaseContextRunner.runWithDatabase(tenantId, () -> service.doWork());
```

## Building & Testing Locally

### Build All Modules

```bash
./gradlew build
```

### Test All Modules

```bash
./gradlew test
```

### Build or Test a Single Module

```bash
./gradlew :msp-domain-jpa-starter:build
./gradlew :msp-domain-jpa-starter:test
```

### Full Check (build + test + jacoco + sonar inputs)

```bash
./gradlew check
```

### View Test Reports

After running `./gradlew test` or `./gradlew check`, JaCoCo HTML coverage reports are generated in:

```
<module>/build/reports/jacoco/test/html/index.html
```

## Publishing

Each module applies the `com.stano.maven-central-publish` Gradle plugin, which configures POM metadata, GPG signing, javadoc/sources jars, and a `publishToMavenCentral` task that uploads directly to the Maven Central Portal's publisher API.

Before publishing, configure a Central Portal user token in `~/.gradle/gradle.properties`:

```properties
com.stano.maven.central.token=your-central-portal-token
```

(or set the `MAVEN_TOKEN` environment variable instead). GPG signing credentials (`signing.keyId`, `signing.password`, `signing.secretKeyRingFile`, or the in-memory equivalents) must also be configured locally.

Publish all modules in one command — running the task from the root fans out to every subproject that has it:

```bash
./gradlew publishToMavenCentral
```

The version is set in `gradle.properties` (`version=0.2.0`). Maven Central does not accept `-SNAPSHOT` versions via this flow, so bump it to a real release version before publishing.

## Module Dependency Graph

```
msp-dependencies-bom (pins 3rd-party versions; no msp-* deps)
msp-bom → msp-dependencies-bom (re-exports a version constraint for every msp-* module)

msp-logging (no msp-* deps)
msp-crypto (no msp-* deps)
  ↓
msp-common → msp-logging
msp-test-starter → msp-logging
  ↓
msp-jackson → msp-common
msp-data-source-starter → msp-common
msp-application-services-starter → msp-logging
msp-rest-api-starter → msp-logging
msp-spring-security-starter → msp-logging
msp-spring-test-starter → msp-test-starter
  ↓
msp-domain-jpa-starter → msp-common, msp-crypto, msp-data-source-starter, msp-jackson(impl), msp-logging(impl), msp-spring-boot-application(compileOnly)
msp-schema-starter → msp-data-source-starter, msp-common, msp-logging(impl)
msp-spring-boot-application → msp-jackson, msp-logging, msp-spring-security-starter, msp-common
msp-rest-api-test-starter → msp-rest-api-starter, msp-spring-test-starter
msp-spring-security-test-starter → msp-logging, msp-spring-security-starter
  ↓
msp-domain-jpa-test-starter → msp-domain-jpa-starter, msp-schema-starter, msp-spring-test-starter
```

## Development Guidelines

See `AGENTS.md` for detailed development guidelines including:
- Gradle conventions and commands
- Java 21 + Kotlin DSL build scripts
- Package naming (`com.stano.<module_domain>`)
- Code style (2-space indent, LF, UTF-8)
- Design patterns (Factory, Abstract Base Classes, `@AutoConfiguration`)
- Test naming and assertion conventions
