dependencies {
  api("com.fasterxml.jackson.core:jackson-annotations")
  api("tools.jackson.core:jackson-core")
  api("tools.jackson.core:jackson-databind")
  api("tools.jackson.dataformat:jackson-dataformat-xml")

  api(project(":msp-types"))

  testImplementation(project(":msp-test-starter"))
}
