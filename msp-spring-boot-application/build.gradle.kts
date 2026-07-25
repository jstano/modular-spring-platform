sourceSets {
  create("otelE2eTest") {
    java.srcDir("src/otelE2eTest/java")
    resources.srcDir("src/otelE2eTest/resources")
    // Deliberately does NOT include sourceSets["test"].output: that source set's
    // src/test/resources/logback-test.xml is one of Logback's own self-initializing filenames,
    // and Spring Boot defers to it (skipping logback-spring.xml entirely) whenever it's on the
    // classpath — which would defeat the point of this test.
    compileClasspath += sourceSets["main"].output
    runtimeClasspath += sourceSets["main"].output
  }
}

configurations {
  named("otelE2eTestImplementation") { extendsFrom(configurations["testImplementation"]) }
  named("otelE2eTestRuntimeOnly") { extendsFrom(configurations["testRuntimeOnly"]) }
}

// Resolves the real OTel javaagent jar to attach to the forked otelE2eTest JVM. Not transitive:
// it's a single shaded/fat jar and should pull in nothing else.
val otelJavaagent by configurations.creating {
  isCanBeConsumed = false
  isCanBeResolved = true
}

dependencies {
  api("org.springframework.boot:spring-boot-starter")

  api(project(":msp-jackson"))
  api(project(":msp-logging"))
  api(project(":msp-spring-security-starter"))
  api(project(":msp-common"))

  implementation("jakarta.persistence:jakarta.persistence-api")
  implementation("org.springframework:spring-orm")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-tomcat")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui")
  implementation("io.micrometer:micrometer-registry-prometheus")
  implementation("io.opentelemetry.instrumentation:opentelemetry-logback-appender-1.0")

  testImplementation(project(":msp-spring-test-starter"))
  testImplementation(project(":msp-spring-security-test-starter"))
  testImplementation("org.springframework.boot:spring-boot-webmvc-test")
  testImplementation("io.opentelemetry:opentelemetry-sdk")
  testImplementation("io.opentelemetry:opentelemetry-sdk-testing")

  // Version literal is deliberate here (unlike other deps in this file): resolving it via
  // msp-dependencies-bom's enforced platform on a standalone configuration also drags in that
  // bom's unrelated transitive graph (a pre-existing quirk, not specific to this dependency).
  // Keep in sync with the io.opentelemetry.javaagent:opentelemetry-javaagent pin in
  // msp-dependencies-bom/build.gradle.kts.
  otelJavaagent("io.opentelemetry.javaagent:opentelemetry-javaagent:2.29.0")
}

// Boots the app under the real OTel javaagent to prove log-record export end to end.
// Deliberately NOT wired into test/check/build: full JVM + Spring Boot + agent premain boot,
// an order of magnitude slower than the rest of this module's tests. Run explicitly via
// `./gradlew :msp-spring-boot-application:otelE2eTest`.
val otelE2eTest by tasks.registering(Test::class) {
  description = "Verifies the real OTel javaagent exports MDC-attributed log records (not part of build/test/check)."
  group = "verification"
  testClassesDirs = sourceSets["otelE2eTest"].output.classesDirs
  classpath = sourceSets["otelE2eTest"].runtimeClasspath

  jvmArgumentProviders.add(CommandLineArgumentProvider {
    listOf("-javaagent:${otelJavaagent.singleFile.absolutePath}")
  })

  environment.putAll(
    mapOf(
      "OTEL_SERVICE_NAME" to "msp-spring-boot-application-otel-e2e-test",
      "OTEL_TRACES_EXPORTER" to "none",
      "OTEL_METRICS_EXPORTER" to "none",
      "OTEL_LOGS_EXPORTER" to "logging",
      "OTEL_INSTRUMENTATION_LOGBACK_APPENDER_ENABLED" to "true",
      "OTEL_INSTRUMENTATION_LOGBACK_APPENDER_EXPERIMENTAL_CAPTURE_MDC_ATTRIBUTES" to "*",
      "OTEL_BLRP_SCHEDULE_DELAY" to "100",
    )
  )
}
