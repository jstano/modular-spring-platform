plugins {
  id("java-platform")
  `maven-publish`
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

publishing {
  publications {
    create<MavenPublication>("maven") {
      from(components["javaPlatform"])
    }
  }
}
