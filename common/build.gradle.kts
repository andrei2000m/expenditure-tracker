//plugins {
//    kotlin("multiplatform")
//    id("org.jetbrains.compose") version "1.3.1"
//    id("com.android.library")
//}
//
//group = "me.andrei2000m"
//version = "1.0-SNAPSHOT"
//
//val ktorVersion = "2.2.1"
////val logbackVersion = "1.4.5"
//
//kotlin {
//    android()
////    js {
////        browser {
////        }
////        binaries.executable()
////    }
//    sourceSets {
//        val commonMain by getting {
//            dependencies {
//                api(compose.runtime)
//                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
//                api("org.kodein.di:kodein-di-framework-compose:7.10.0")
//
//                implementation("io.ktor:ktor-server-core:$ktorVersion")
//                implementation("io.ktor:ktor-server-netty:$ktorVersion")
//                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
//                implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
//                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
//                implementation("io.ktor:ktor-server-html-builder-jvm:$ktorVersion")
//                implementation("io.ktor:ktor-server-config-yaml:$ktorVersion")
//                implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.8.0")
//
//                implementation("org.jetbrains.exposed:exposed-core:0.40.1")
//                implementation("org.jetbrains.exposed:exposed-dao:0.40.1")
//                implementation("org.jetbrains.exposed:exposed-jdbc:0.40.1")
//                implementation("org.jetbrains.exposed:exposed-java-time:0.40.1")
//                implementation("org.xerial:sqlite-jdbc:3.40.0.0")
//                implementation("com.zaxxer:HikariCP:5.0.1")
//
//                implementation("ch.qos.logback:logback-classic:$logbackVersion")
//            }
//        }
//        val commonTest by getting {
//            dependencies {
//                implementation(kotlin("test"))
//
//                implementation("io.ktor:ktor-server-test-host:$ktorVersion")
//                implementation("org.junit.jupiter:junit-jupiter-params:5.9.1")
//                implementation("io.kotest:kotest-assertions-core:5.5.4")
//                implementation("io.mockk:mockk:1.13.3")
//            }
//        }
//        val androidMain by getting {
//            dependencies {
//                api("androidx.appcompat:appcompat:1.4.1")
//                api("androidx.core:core-ktx:1.7.0")
//                // ConstraintLayout dependencies
//                implementation("androidx.constraintlayout:constraintlayout:2.1.4")
//                implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
//            }
//        }
////        val androidTest by getting {
////            dependencies {
////                implementation("junit:junit:4.13.2")
////            }
////        }
//    }
//}
//
//android {
//    compileSdk = 33
//    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
//    defaultConfig {
//        minSdk = 24
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_1_8
//        targetCompatibility = JavaVersion.VERSION_1_8
//    }
//}