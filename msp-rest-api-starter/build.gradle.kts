dependencies {
  api(project(":msp-logging"))

  api("org.springframework.boot:spring-boot-starter-web")
  api("org.springdoc:springdoc-openapi-starter-common")

  testImplementation(project(":msp-spring-test-starter"))
}
