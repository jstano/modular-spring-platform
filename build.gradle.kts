import com.stano.gradle.mavencentralpublish.MavenCentralPublishExtension
import org.sonarqube.gradle.SonarExtension

plugins {
  id("com.stano.base")
  id("com.stano.sonar")
  id("com.stano.maven-central-publish") apply false
  id("com.stano.java-library") apply false
  id("java-library")
  id("maven-publish")
  id("jacoco")
  id("java-gradle-plugin")
}

// com.stano.sonar now only applies org.sonarqube when SONAR_HOST_URL/SONAR_TOKEN
// (or the sonar.host.url/sonar.token properties) are configured, so the
// sonarqube {} extension may not exist -- guard on the plugin actually being applied.
val rootProjectForSonar = project
pluginManager.withPlugin("org.sonarqube") {
  rootProjectForSonar.extensions.configure<SonarExtension> {
    properties {
      property("sonar.projectName", "java-platform")
      property("sonar.projectKey", "${rootProjectForSonar.group}:java-platform")
    }
  }
}

val moduleDescriptions = mapOf(
  "msp-application-services-starter" to "Service layer starter with Spring Context and transaction support for service-layer beans.",
  "msp-common" to "Foundational types and utilities with no Spring dependency: exception hierarchy, @Generated annotation, UUID generation, Apache Commons, jOOR, Moneta money.",
  "msp-crypto" to "AES-256 encryption, SHA-256 hashing, Base64 utilities.",
  "msp-data-source-starter" to "Multi-tenant RoutableDataSource, DatabaseContextHolder, and DataSourceFactory for dynamic datasource switching.",
  "msp-domain-jpa-starter" to "JPA/Hibernate with multi-tenant RoutableDataSource, Envers audit, custom PostgreSQL dialect.",
  "msp-domain-jpa-test-starter" to "JPA integration test support with embedded PostgreSQL.",
  "msp-jackson" to "Jackson ObjectMapperFactory singleton and JSON utility class.",
  "msp-logging" to "Structured logging (SemanticLogger, LoggingContext, Logback/JSON output).",
  "msp-rest-api-starter" to "Spring MVC REST starter: web and Springdoc OpenAPI UI.",
  "msp-rest-api-test-starter" to "REST API test starter; composes msp-rest-api-starter and msp-spring-test-starter for controller-layer tests.",
  "msp-schema-starter" to "SchemaManager and Spring auto-configuration for Flyway-based schema migrations.",
  "msp-spring-boot-application" to "Spring Boot bootstrap: UTC timezone, startup logging, auto-configuration, OpenAPI, Prometheus metrics.",
  "msp-spring-boot-gradle-plugin" to "Bundles spring-boot-gradle-plugin and spring-boot-devtools.",
  "msp-spring-security-starter" to "Spring Security starter (core, OAuth2).",
  "msp-spring-security-test-starter" to "Security test starter; adds spring-security-test for secured-endpoint testing.",
  "msp-spring-test-starter" to "Spring-aware test support (boot-test, security-test, kotest-extensions-spring).",
  "msp-test-starter" to "Base test library: JUnit 5, Mockito, AssertJ, Kotest, Google Truth."
)

configure(javaProjects()) {
  apply(plugin = "com.stano.java-library")
  apply(plugin = "com.stano.maven-central-publish")

  configurations {
    all {
      exclude(group = "commons-logging", module = "commons-logging")
    }
  }

  dependencies {
    api(enforcedPlatform(project(":msp-dependencies-bom")))
    add("annotationProcessor", enforcedPlatform(project(":msp-dependencies-bom")))
  }

  extensions.configure<MavenCentralPublishExtension> {
    componentName = "java"
    pomName = name
    pomDescription = moduleDescriptions[name] ?: name
    pomUrl = "https://github.com/jstano/modular-spring-platform"
    licenseName = "Apache License, Version 2.0"
    licenseUrl = "https://www.apache.org/licenses/LICENSE-2.0"
    developerId = "jstano"
    developerName = "Jeff Stano"
    developerEmail = "jeff@stano.com"
    scmConnection = "scm:git:https://github.com/jstano/modular-spring-platform.git"
    scmDeveloperConnection = "scm:git:ssh://git@github.com:jstano/modular-spring-platform.git"
    scmUrl = "https://github.com/jstano/modular-spring-platform"
  }

  tasks.withType<Test>().configureEach {
    systemProperty("msp.encryption.secret", "test-secret-key")
    jvmArgs("--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED")
  }
}

fun javaProjects(): Set<Project> = subprojects - nonJavaProjects()
fun nonJavaProjects(): Set<Project> = subprojects.filter { project ->
  project.name == "msp-bom" || project.name == "msp-dependencies-bom"
}.toSet()
