import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.4.20"
  kotlin("kapt") version "1.4.20"
  id("org.jetbrains.compose") version "0.3.0-build134"
  id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "io.github.paulblessing"
version = "1.0.6"

repositories {
  jcenter()
  mavenCentral()
  maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

dependencies {
  implementation(compose.desktop.macos_x64)
  implementation(compose.desktop.windows_x64)
  implementation(compose.desktop.linux_x64)
  implementation("com.squareup.moshi:moshi:1.11.0")
  implementation("com.squareup.okio:okio:2.9.0")
  kapt("com.squareup.moshi:moshi-kotlin-codegen:1.11.0")
  testImplementation("junit:junit:4.12")
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<ProcessResources> {
  doLast {
    val resourcesDir = sourceSets.main.get().output.resourcesDir!!
    resourcesDir.mkdirs()
    val file = File(resourcesDir, "build.properties")
    val ver = project.version
    file.writeText("tracker.version=$ver")
  }
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xinline-classes", "-Xopt-in=androidx.compose.foundation.layout.ExperimentalLayout")
    jvmTarget = "11"
  }
}

tasks.withType<Jar> {
  manifest {
    attributes["Main-Class"] = "io.github.paulblessing.kh2randotracker.Tracker"
  }
}

compose.desktop {
  application {
    mainClass = "io.github.paulblessing.kh2randotracker.Tracker"
    nativeDistributions {
      targetFormats(TargetFormat.Msi)
      packageName = "KH2 Randomizer Tracker"
      description = "KH2 Randomizer Tracker"
      copyright = "© 2020 Paul Blessing"
      modules("java.logging")

      macOS {
        iconFile.set(file("src/main/resources/images/classic/replica_data.icns"))
      }

      windows {
        shortcut = true
        iconFile.set(file("src/main/resources/images/classic/replica_data.ico"))
      }

      linux {
        iconFile.set(file("src/main/resources/images/classic/replica_data.png"))
      }
    }
  }
}
