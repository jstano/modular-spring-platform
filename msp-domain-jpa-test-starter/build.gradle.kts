dependencies {
  api(project(":msp-domain-jpa-starter"))
  api(project(":msp-spring-test-starter"))

//  api("com.stano:schema-installer-flyway")
  api("io.zonky.test.postgres:embedded-postgres-binaries-darwin-arm64v8")
  api("io.zonky.test:embedded-database-spring-test")
  api("io.zonky.test:embedded-database-spring-test-autoconfigure")
  api("io.zonky.test:embedded-postgres")
}
