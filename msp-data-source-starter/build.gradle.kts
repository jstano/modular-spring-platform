dependencies {
  api(project(":msp-common"))

  api("com.zaxxer:HikariCP")
  api("org.postgresql:postgresql")
  api("org.springframework.boot:spring-boot-starter-jdbc")
  api("org.springframework:spring-core")
  api("org.apache.commons:commons-lang3")

  implementation("commons-io:commons-io")

  testImplementation(project(":msp-spring-test-starter"))
  testImplementation("com.h2database:h2")
  testImplementation("org.mockito:mockito-core")
}
