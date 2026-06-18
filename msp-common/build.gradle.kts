dependencies {
  api("commons-codec:commons-codec")
  api("commons-io:commons-io")
  api("org.apache.commons:commons-collections4")
  api("org.apache.commons:commons-csv")
  api("org.apache.commons:commons-lang3")
  api("org.apache.commons:commons-text")
  api("org.javamoney:moneta")
  api("org.jooq:joor")
  api("com.github.f4b6a3:uuid-creator")

  implementation(project(":msp-logging"))
  implementation("org.apache.tika:tika-core")

  testImplementation(project(":msp-test-starter"))
}
