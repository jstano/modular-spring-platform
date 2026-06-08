dependencies {
  api(project(":msp-logging"))

  api("com.google.truth:truth")
  api("net.bytebuddy:byte-buddy")
  api("net.bytebuddy:byte-buddy-agent")
  api("org.apache.httpcomponents:httpclient")
  api("org.apache.httpcomponents:httpmime")
  api("org.assertj:assertj-core")
  api("org.hamcrest:hamcrest-core")
  api("org.junit.jupiter:junit-jupiter")
  api("org.junit.platform:junit-platform-launcher")
  api("org.mockito:mockito-junit-jupiter")
  api("org.objenesis:objenesis")
  api("org.skyscreamer:jsonassert")

  api("io.kotest:kotest-assertions-core")
  api("io.kotest:kotest-property")
  api("io.kotest:kotest-runner-junit5")
}
