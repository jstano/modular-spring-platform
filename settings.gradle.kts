import com.stano.gradle_dependency_management.MavenRepositoryUtil

rootProject.name = "modular-spring-platform"

buildscript {
  val properties = getExtensions().extraProperties.properties
  val stanoMavenUrl = properties["com.stano.maven.url"]?.toString() ?: System.getenv("STANO_MAVEN_URL")
  val stanoMavenUsername = properties["com.stano.maven.username"]?.toString() ?: System.getenv("STANO_MAVEN_USERNAME")
  val stanoMavenPassword = properties["com.stano.maven.password"]?.toString() ?: System.getenv("STANO_MAVEN_PASSWORD")

  repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
    maven {
      name = "stano-maven"
      url = uri(stanoMavenUrl)
      credentials {
        username = stanoMavenUsername
        password = stanoMavenPassword
      }
    }
  }

  dependencies {
    classpath("com.stano:gradle-dependency-management:${properties["gradleDependencyManagementVersion"].toString()}")
    classpath("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:${properties["sonarPluginVersion"]}")
    classpath("com.diffplug.spotless:spotless-plugin-gradle:${properties["spotlessVersion"]}")
  }
}

MavenRepositoryUtil.configureDependencyResolutionManagement(extensions, dependencyResolutionManagement)

include("msp-application-services-starter")
include("msp-bom")
include("msp-crypto")
include("msp-data-source-starter")
include("msp-dependencies-bom")
include("msp-domain-jpa-starter")
include("msp-domain-jpa-test-starter")
include("msp-jackson")
include("msp-logging")
include("msp-rest-api-starter")
include("msp-rest-api-test-starter")
include("msp-schema-starter")
include("msp-spring-boot-application")
include("msp-spring-boot-gradle-plugin")
include("msp-spring-security-starter")
include("msp-spring-security-test-starter")
include("msp-spring-test-starter")
include("msp-test-starter")
include("msp-common")
