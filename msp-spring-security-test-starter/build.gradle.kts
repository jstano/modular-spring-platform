dependencies {
  api(project(":msp-logging"))
  api(project(":msp-spring-security-starter"))

  api("org.springframework.security:spring-security-test")

  testImplementation(project(":msp-test-starter"))
}
