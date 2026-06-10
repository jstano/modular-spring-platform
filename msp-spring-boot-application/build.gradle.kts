dependencies {
  api("org.springframework.boot:spring-boot-starter")

  api(project(":msp-jackson"))
  api(project(":msp-logging"))

  implementation("org.springframework.boot:spring-boot-starter-tomcat")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui")
  implementation("io.opentelemetry.javaagent:opentelemetry-javaagent")
  implementation("io.opentelemetry.instrumentation:opentelemetry-logback-appender-1.0")

  implementation("io.micrometer:micrometer-registry-prometheus")
  implementation("org.jolokia:jolokia-core")
  implementation("org.jolokia:jolokia-agent-jvm")

  testImplementation(project(":msp-test-starter"))
}
