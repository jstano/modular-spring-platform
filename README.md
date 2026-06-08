# Modular Spring Platform

An opinionated Spring Boot platform library that standardizes cross-cutting concerns across microservices: logging, encryption, JSON serialization, JPA with multi-tenancy, REST APIs, security, and testing infrastructure.

## Requirements

- **Java:** 21 (source and target compatibility)
- **Gradle:** 8.14+ (wrapper included)
- **Spring Boot:** 4.0.6 (managed by BOM)

## Using the Platform

### 1. Configure the Maven Repository

Add the private Stano Maven repository to `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
  repositories {
    mavenCentral()
    maven {
      name = "stano-maven"
      url = uri(System.getenv("STANO_MAVEN_URL"))
      credentials {
        username = System.getenv("STANO_MAVEN_USERNAME")
        password = System.getenv("STANO_MAVEN_PASSWORD")
      }
    }
  }
}
```

Set the environment variables:
- `STANO_MAVEN_URL` — Maven repository URL
- `STANO_MAVEN_USERNAME` — Authentication username
- `STANO_MAVEN_PASSWORD` — Authentication password

### 2. Import the BOM

Add the aggregator BOM to your `build.gradle.kts`:

```kotlin
dependencies {
  implementation(enforcedPlatform("com.stano:msp-bom:1.0.0-SNAPSHOT"))
  
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
| **`msp-types`** | Core exception types and utility libraries (Apache Commons, jOOR reflection, Moneta money, JSR-354) | ✓ |
| **`msp-logging`** | Structured logging with `SemanticLogger`, `LoggingContext`, logback JSON output | ✓ |
| **`msp-crypto`** | AES-256 encryption, SHA-256 hashing, Base64 utilities (text, binary, passwords) | ✓ |
| **`msp-jackson`** | Jackson 3.x `ObjectMapperFactory` singleton and `JSON` utility class | ✓ |
| **`msp-util`** | General-purpose utilities (aggregator for common dependencies) | ✓ |
| **`msp-application-services-starter`** | Service layer starter with Spring Context and transaction support |  |
| **`msp-spring-boot-application`** | Spring Boot bootstrap with OpenAPI, Prometheus metrics, health/metrics endpoints |  |
| **`msp-rest-api-starter`** | Spring MVC REST starter with Springdoc OpenAPI UI |  |
| **`msp-spring-security-starter`** | Spring Security starter (core, OAuth2) |  |
| **`msp-domain-jpa-starter`** | JPA/Hibernate with multi-tenant `RoutableDataSource`, audit, custom PostgreSQL dialect |  |
| **`msp-spring-boot-gradle-plugin`** | Re-exports spring-boot-gradle-plugin and spring-boot-devtools |  |
| **`msp-test-starter`** | Base test library (JUnit 5, Mockito, AssertJ, Kotest) |  |
| **`msp-spring-test-starter`** | Spring-aware test support |  |
| **`msp-rest-api-test-starter`** | REST controller test starter |  |
| **`msp-spring-security-test-starter`** | Security-layer test support |  |
| **`msp-domain-jpa-test-starter`** | JPA integration tests with embedded PostgreSQL |  |

### Key APIs by Module

#### `msp-spring-boot-application`
- `MspSpringApplication.run(Class<?>, String...)` — Application bootstrap entry point
- `HealthController`, `MetricsController` — Health and metrics endpoints

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

#### `msp-domain-jpa-starter`
- `DatabaseContextHolder.setDatabaseId(Long)` / `getDatabaseId()` / `clear()` — Multi-tenant context (ThreadLocal)
- `RoutableDataSource` — Dynamic routing datasource for multi-tenancy
- `RoutableDataSourcesLoader<Long>` — Interface to implement for loading tenant datasources
- `AbstractPersistenceAdapterSpringConfig` — Base class for persistence configuration
- `Query`, `QuerySingleResult`, `Update`, `Parameters` — Spring JDBC helpers
- `ReadOnlyRepository` — Read-only JPA repository interface
- `CustomPostgreSQLDialect` — PostgreSQL-specific Hibernate dialect
- Attribute converters for encrypted fields: `EncryptedTextAttributeConverter`, `EncryptedBytesAttributeConverter`, `PasswordAttributeConverter`

#### `msp-types`
Exception hierarchy for standard error responses:
- `BadRequestException` — HTTP 400
- `UnauthorizedException` — HTTP 401
- `ForbiddenException` — HTTP 403
- `ResourceNotFoundException` — HTTP 404
- `ResourceConflictException` — HTTP 409
- `ResourceLockedException` — HTTP 423
- `InvalidRequestException` — HTTP 422
- `InternalServerError` — HTTP 500
- `ServiceUnavailableException` — HTTP 503

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

Implement `RoutableDataSourcesLoader<Long>` to define how tenant datasources are discovered:

```java
@Configuration
public class MyPersistenceConfig extends AbstractPersistenceAdapterSpringConfig {
  @Bean
  public RoutableDataSourcesLoader<Long> dataSourcesLoader() {
    return tenantId -> {
      // Load datasource for the given tenant ID
      return createDataSourceForTenant(tenantId);
    };
  }
}
```

In your request handler, set the current tenant context:

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

Publish all modules to the Stano Maven repository:

```bash
STANO_MAVEN_URL=https://... \
STANO_MAVEN_USERNAME=... \
STANO_MAVEN_PASSWORD=... \
./gradlew publish
```

The version is hardcoded as `1.0.0-SNAPSHOT` in `settings.gradle.kts`.

## Module Dependency Graph

```
msp-types (no deps)
  ↓
msp-logging (no deps)
msp-crypto (no deps)
  ↓
msp-jackson → msp-types
msp-util → msp-types + msp-logging
msp-application-services-starter → msp-logging

msp-domain-jpa-starter → msp-crypto, msp-types, msp-jackson(impl)
msp-rest-api-starter → msp-logging
msp-spring-security-starter → msp-logging
msp-spring-boot-application → msp-jackson(impl), msp-logging(impl)

Test starters:
msp-test-starter → msp-logging
msp-spring-test-starter → msp-test-starter
msp-rest-api-test-starter → msp-rest-api-starter, msp-spring-test-starter
msp-spring-security-test-starter → msp-logging, msp-spring-security-starter
msp-domain-jpa-test-starter → msp-domain-jpa-starter, msp-spring-test-starter
```

## Development Guidelines

See `AGENTS.md` for detailed development guidelines including:
- Gradle conventions and commands
- Java 21 + Kotlin DSL build scripts
- Package naming (`com.stano.<module_domain>`)
- Code style (2-space indent, LF, UTF-8)
- Design patterns (Factory, Abstract Base Classes, `@AutoConfiguration`)
- Test naming and assertion conventions
