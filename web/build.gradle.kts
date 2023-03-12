plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "1.3.1"
}

group = "me.andrei2000m"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    js(IR) {
        browser{
        }
        binaries.executable()
    }
    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(compose.web.core)
                implementation(compose.runtime)
                //implementation(project(":common"))
            }
        }
    }
}