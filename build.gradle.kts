import com.stano.gradle_dependency_management.MavenRepositoryUtil

plugins {
  id("org.sonarqube")
  id("java-library")
  id("maven-publish")
  id("jacoco")
  id("java-gradle-plugin")
}

sonarqube {
  val extraProperties = extensions.extraProperties.properties
  val sonarHost = extraProperties["com.stano.sonar.host.url"]?.toString() ?: System.getenv("STANO_SONAR_HOST_URL")
  val sonarToken = extraProperties["com.stano.sonar.token"]?.toString() ?: System.getenv("STANO_SONAR_TOKEN")

  properties {
    property("sonar.host.url", sonarHost)
    property("sonar.token", sonarToken)
    property("sonar.projectName", "java-platform")
    property("sonar.projectKey", "${project.group}:java-platform")
    property("sonar.projectVersion", project.version)
  }
}

configure(javaProjects()) {
  apply(plugin = "java-library")
  apply(plugin = "maven-publish")
  apply(plugin = "jacoco")
  apply(plugin = "com.diffplug.spotless")

  configurations {
    all {
      exclude(group = "commons-logging", module = "commons-logging")
    }
  }

  dependencies {
    api(enforcedPlatform(project(":msp-dependencies-bom")))
  }

  MavenRepositoryUtil.configurePublishing(this)

  tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs = compilerOptions()
    sourceCompatibility = "21"
    targetCompatibility = "21"
  }

  java {
    withJavadocJar()
    withSourcesJar()
  }

  tasks.withType<Jar> {
    exclude("**/.gitkeep")
  }
  tasks.withType<Javadoc>().configureEach {
    (options as CoreJavadocOptions).addStringOption("Xdoclint:none", "-quiet")
  }
  tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    jvmArgs("--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED", "--add-opens", "java.base/java.lang=ALL-UNNAMED")
    systemProperty("msp.encryption.secret", "test-secret-key")
    finalizedBy("jacocoTestReport")
  }
  tasks.withType<JacocoReport>().configureEach {
    reports {
      html.required.set(true)
      xml.required.set(true)
    }
  }

  tasks.withType<GenerateModuleMetadata>().configureEach {
    // The value "enforced-platform" is provided in the validation error message you got
    suppressedValidationErrors.add("enforced-platform")
  }

  configure<com.diffplug.gradle.spotless.SpotlessExtension> {
    java {
      googleJavaFormat("1.35.0")
        .reflowLongStrings()
        .formatJavadoc(true)
      endWithNewline()
      expandWildcardImports()
      importOrder()
      removeUnusedImports()
      trimTrailingWhitespace()
    }
  }

  tasks.named("check") {
    dependsOn("spotlessCheck")
  }
}

fun javaProjects(): Set<Project> = subprojects - nonJavaProjects()
fun nonJavaProjects(): Set<Project> = subprojects.filter { project ->
  project.name == "msp-bom" || project.name == "msp-dependencies-bom"
}.toSet()
fun compilerOptions(): List<String> = listOf("-Xlint:none", "-Xdoclint:none", "-nowarn", "-parameters")
