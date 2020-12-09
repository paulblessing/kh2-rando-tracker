import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.4.20"
  kotlin("kapt") version "1.4.20"
  id("org.jetbrains.compose") version "0.2.0-build132"
}

group = "io.github.paulblessing"
version = "1.0"

repositories {
  jcenter()
  mavenCentral()
  maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

dependencies {
  implementation(compose.desktop.currentOs)
  implementation("com.squareup.moshi:moshi:1.11.0")
  implementation("com.squareup.okio:okio:2.9.0")
  kapt("com.squareup.moshi:moshi-kotlin-codegen:1.11.0")
  testImplementation("junit:junit:4.12")
}

tasks.withType<KotlinCompile>() {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xinline-classes", "-Xopt-in=androidx.compose.foundation.layout.ExperimentalLayout")
    jvmTarget = "11"
  }
}

compose.desktop {
  application {
    mainClass = "io.github.paulblessing.kh2randotracker.Tracker"
    nativeDistributions {
      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
      packageName = "KH2 Randomizer Tracker"
      description = "KH2 Randomizer Tracker"
      copyright = "© 2020 Paul Blessing"
      modules("java.logging")

      macOS {
        iconFile.set(file("src/main/resources/images/classic/replica_data.icns"))
      }
    }
  }
}
