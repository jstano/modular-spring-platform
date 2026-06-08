dependencies {
  api(project(":msp-types"))

  implementation("org.apache.commons:commons-csv")
  implementation("org.apache.tika:tika-core")

  implementation(project(":msp-logging"))

  testImplementation(project(":msp-test-starter"))
}
