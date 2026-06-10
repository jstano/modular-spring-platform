plugins {
  `java-platform`
  `maven-publish`
}

javaPlatform {
  allowDependencies()
}

val springBootVersion: String by project
val pactVersion: String by project

dependencies {
  api(platform("org.springframework.boot:spring-boot-dependencies:${springBootVersion}"))
  api(platform("io.kotest:kotest-bom:6.1.11"))
  api(platform("io.zonky.test.postgres:embedded-postgres-binaries-bom:18.4.0"))
  api(platform("com.stano:schema-bom:0.30.0"))
  api(platform("org.javamoney:moneta:1.4.5"))

  constraints {
    // stano
    api("com.stano:date-range:0.99.0")

    // jakarta EE
    api("jakarta.el:jakarta.el-api:6.0.1")
    api("org.glassfish.expressly:expressly:6.0.0")
    api("jakarta.interceptor:jakarta.interceptor-api:2.2.0")

    // misc 3rd party
    api("com.damnhandy:handy-uri-templates:2.1.8")
    api("com.github.f4b6a3:uuid-creator:6.1.1")
    api("com.google.guava:guava:33.6.0-jre")
    api("com.googlecode.owasp-java-html-sanitizer:owasp-java-html-sanitizer:20260313.1")
    api("com.javadocmd:simplelatlng:1.4.0")
    api("com.mattbertolini:liquibase-slf4j:5.1.0")
    api("org.atteo:evo-inflector:2.0")
    api("org.jooq:joor:0.9.15")
    api("net.sf.saxon:Saxon-HE:13.0")
    api("org.owasp.esapi:esapi:2.7.0.0")
//    api("com.github.ben-manes.caffeine:caffeine:3.2.4")
    api("com.cronutils:cron-utils:9.2.1")
    api("io.hypersistence:hypersistence-utils-hibernate-70:3.15.3")
    api("org.apache.tika:tika-core:3.3.1")

    // apache
    api("commons-beanutils:commons-beanutils:1.11.0")
    api("commons-cli:commons-cli:1.11.0")
    api("commons-io:commons-io:2.22.0")
    api("commons-net:commons-net:3.13.0")
    api("org.apache.ant:ant:1.10.17")
    api("org.apache.commons:commons-collections4:4.5.0")
    api("org.apache.commons:commons-csv:1.14.1")
    api("org.apache.commons:commons-text:1.15.0")
    api("org.apache.httpcomponents:fluent-hc:4.5.14")
    api("org.apache.httpcomponents:httpclient:4.5.14")
    api("org.apache.httpcomponents:httpmime:4.5.14")
    api("org.apache.poi:ooxml-schemas:1.4")
    api("org.apache.poi:poi-ooxml:5.5.1")
    api("org.apache.poi:poi:5.5.1")

    // logging
    api("net.logstash.logback:logstash-logback-encoder:9.0")
    api("uk.org.lidalia:sysout-over-slf4j:1.0.2")

    // spring boot plugin artifacts
    api("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")

    // jolokia
    api("org.jolokia:jolokia-core:2.6.0")
    api("org.jolokia:jolokia-agent-jvm:2.6.0")

    // springdoc (not in spring BOMs)
    api("org.springdoc:springdoc-openapi-starter-common:3.0.3")
    api("org.springdoc:springdoc-openapi-starter-webmvc-api:3.0.3")
    api("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.3")
    api("org.springdoc:springdoc-openapi-starter-webflux-api:3.0.3")
    api("org.springdoc:springdoc-openapi-starter-webflux-ui:3.0.3")

    // pact
    api("au.com.dius.pact:consumer:${pactVersion}")
    api("au.com.dius.pact.consumer:java8:${pactVersion}")
    api("au.com.dius.pact.consumer:junit:${pactVersion}")
    api("au.com.dius.pact.consumer:junit5:${pactVersion}")
    api("au.com.dius.pact:provider:${pactVersion}")
    api("au.com.dius.pact.provider:gradle:${pactVersion}")
    api("au.com.dius.pact.provider:junit5:${pactVersion}")
    api("au.com.dius.pact.provider:junit5spring:${pactVersion}")
    api("au.com.dius.pact.provider:spring:${pactVersion}")

    // testing tools
    api("org.objenesis:objenesis:3.5")
    api("com.github.codemonstur:embedded-redis:1.4.3")
    api("com.icegreen:greenmail:2.1.8")
    api("io.zonky.test:embedded-postgres:2.2.2")
    api("io.zonky.test:embedded-database-spring-test:2.8.0")
    api("io.zonky.test:embedded-database-spring-test-autoconfigure:2.8.0")
    api("org.mockftpserver:MockFtpServer:3.2.0")
    api("com.google.truth:truth:1.4.5")
  }
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      from(components["javaPlatform"])
    }
  }
}
