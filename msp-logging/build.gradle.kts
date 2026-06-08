dependencies {
  api("ch.qos.logback:logback-classic")
  api("ch.qos.logback:logback-core")
  api("org.slf4j:jcl-over-slf4j")
  api("org.slf4j:jul-to-slf4j")
  api("org.slf4j:log4j-over-slf4j")
  api("org.slf4j:slf4j-api")
  api("uk.org.lidalia:sysout-over-slf4j")
  api("net.logstash.logback:logstash-logback-encoder")

  testImplementation(project(":msp-test-starter"))
}
