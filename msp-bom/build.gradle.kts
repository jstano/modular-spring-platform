import com.stano.gradle.mavencentralpublish.MavenCentralPublishExtension

plugins {
  id("java-platform")
  `maven-publish`
  id("com.stano.maven-central-publish")
}

javaPlatform {
  allowDependencies()
}

dependencies {
  api(platform(project(":msp-dependencies-bom")))

  constraints {
    val projectsToAdd = project.rootProject.subprojects.filter {
      it.name != "msp-bom" && it.name != "msp-dependencies-bom"
    }
    projectsToAdd.forEach { p ->
      api("${p.group}:${p.name}:${p.version}")
    }
  }
}

extensions.configure<MavenCentralPublishExtension> {
  componentName = "javaPlatform"
  pomName = "MSP BOM"
  pomDescription = "Maven BOM for the modular-spring-platform project."
  pomUrl = "https://github.com/jstano/modular-spring-platform"
  licenseName = "Apache License, Version 2.0"
  licenseUrl = "https://www.apache.org/licenses/LICENSE-2.0"
  developerId = "jstano"
  developerName = "Jeff Stano"
  developerEmail = "jeff@stano.com"
  scmConnection = "scm:git:https://github.com/jstano/modular-spring-platform.git"
  scmDeveloperConnection = "scm:git:ssh://git@github.com:jstano/modular-spring-platform.git"
  scmUrl = "https://github.com/jstano/modular-spring-platform"
}
