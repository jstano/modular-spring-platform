dependencies {
  api(project(":msp-logging"))
  api(project(":msp-spring-security-starter"))

  api("org.springframework.boot:spring-boot-starter-test")
  api("org.springframework.boot:spring-boot-test-autoconfigure")
  api("org.springframework.security:spring-security-test")

  testImplementation(project(":msp-test-starter"))
}
