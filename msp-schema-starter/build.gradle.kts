dependencies {
  api(project(":msp-data-source-starter"))
  api(project(":msp-common"))

  api("com.stano:schema-installer-flyway")

  implementation(project(":msp-logging"))

  implementation("org.springframework.boot:spring-boot-starter-jdbc")
  implementation("org.springframework.boot:spring-boot-starter")

  testImplementation(project(":msp-spring-test-starter"))
  testImplementation(project(":msp-domain-jpa-test-starter"))
}
