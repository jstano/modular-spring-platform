rootProject.name = "modular-spring-platform"

pluginManagement {
  repositories {
    mavenLocal()
    gradlePluginPortal()
  }
}

plugins {
  id("com.stano.settings") version "0.1.8-SNAPSHOT"
}

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
