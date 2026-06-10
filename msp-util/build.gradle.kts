dependencies {
  api(project(":msp-types"))
  api("com.github.f4b6a3:uuid-creator")

  implementation("org.apache.commons:commons-csv")
  implementation("org.apache.tika:tika-core")

  implementation(project(":msp-logging"))

  testImplementation(project(":msp-test-starter"))
}
