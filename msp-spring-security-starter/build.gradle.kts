dependencies {
  api(project(":msp-logging"))

  api("org.springframework.boot:spring-boot-starter-security")
  api("org.springframework.security:spring-security-oauth2-core")

  testImplementation(project(":msp-test-starter"))
}
