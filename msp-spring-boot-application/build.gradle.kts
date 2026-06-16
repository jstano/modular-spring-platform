dependencies {
  api("org.springframework.boot:spring-boot-starter")

  api(project(":msp-jackson"))
  api(project(":msp-logging"))
  api(project(":msp-spring-security-starter"))
  api(project(":msp-types"))

  implementation("jakarta.persistence:jakarta.persistence-api")
  implementation("org.springframework:spring-orm")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-tomcat")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui")
  implementation("io.micrometer:micrometer-registry-prometheus")
  implementation("io.opentelemetry.javaagent:opentelemetry-javaagent")
  implementation("io.opentelemetry.instrumentation:opentelemetry-logback-appender-1.0")

  testImplementation(project(":msp-spring-test-starter"))
  testImplementation("org.springframework.boot:spring-boot-webmvc-test")
}
