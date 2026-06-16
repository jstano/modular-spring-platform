dependencies {
  api(project(":msp-logging"))

  api("org.springframework.boot:spring-boot-starter-security")
  api("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

  testImplementation(project(":msp-test-starter"))
}
