dependencies {
  implementation("commons-codec:commons-codec")
  implementation("org.jooq:joor")
  implementation("org.springframework.security:spring-security-crypto")

  testImplementation(project(":msp-test-starter"))
}
