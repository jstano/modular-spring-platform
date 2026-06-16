dependencies {
  api(project(":msp-crypto"))
  api(project(":msp-types"))

  compileOnly(project(":msp-spring-boot-application"))

  api("io.hypersistence:hypersistence-utils-hibernate-70")
  api("jakarta.interceptor:jakarta.interceptor-api")
  api("jakarta.transaction:jakarta.transaction-api")
  api("org.hibernate.orm:hibernate-core")
  api("org.hibernate.orm:hibernate-envers")
  api("org.hibernate.validator:hibernate-validator")
  api("org.postgresql:postgresql")
  api("org.springframework.boot:spring-boot-starter-data-jpa")
  api("org.springframework.boot:spring-boot-starter-jdbc")
  api("org.springframework.data:spring-data-jpa")
  api("org.springframework:spring-jdbc")
  api("org.springframework:spring-orm")
  api("org.springframework:spring-tx")
  api("com.stano:schema-installer-flyway")
  api("org.flywaydb:flyway-core")

  implementation(project(":msp-jackson"))
  implementation(project(":msp-logging"))
  implementation(project(":msp-util"))

  implementation("com.zaxxer:HikariCP")
  implementation("io.micrometer:micrometer-registry-prometheus")
  implementation("org.glassfish.expressly:expressly")
  implementation("org.hibernate.orm:hibernate-micrometer")
  compileOnly("org.springframework:spring-webmvc")

  testImplementation(project(":msp-spring-test-starter"))
  testImplementation(project(":msp-domain-jpa-test-starter"))
  testImplementation("com.h2database:h2")
}
