dependencies {
  implementation(project(":msp-jackson"))
  implementation(project(":msp-logging"))

  implementation("org.springframework.boot:spring-boot-starter")
  implementation("org.springframework.boot:spring-boot-starter-tomcat")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui")

  implementation("io.micrometer:micrometer-registry-prometheus")
  implementation("org.jolokia:jolokia-core")
  implementation("org.jolokia:jolokia-agent-jvm")

  testImplementation(project(":msp-test-starter"))
}
