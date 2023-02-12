import com.github.gradle.node.npm.task.NpxTask
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform") version "1.8.10"
    kotlin("plugin.serialization") version "1.8.10"
    id("com.github.node-gradle.node") version "3.5.1"
    application
}

group = "me.andrei2000m"
version = "1.0-SNAPSHOT"

val ktorVersion = "2.2.1"
val logbackVersion = "1.4.5"
val nodeJsVersion = "16.14.0"
val angularCliVersion = "15.1.3"

repositories {
    jcenter()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

kotlin {
    jvm {
        withJava()
    }
    js {
        browser {
            binaries.executable()
        }
    }
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-server-core:$ktorVersion")
                implementation("io.ktor:ktor-server-netty:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("io.ktor:ktor-server-html-builder-jvm:$ktorVersion")
                implementation("io.ktor:ktor-server-config-yaml:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.8.0")

                implementation("org.jetbrains.exposed:exposed-core:0.40.1")
                implementation("org.jetbrains.exposed:exposed-dao:0.40.1")
                implementation("org.jetbrains.exposed:exposed-jdbc:0.40.1")
                implementation("org.jetbrains.exposed:exposed-java-time:0.40.1")
                implementation("org.xerial:sqlite-jdbc:3.40.0.0")
                implementation("com.zaxxer:HikariCP:5.0.1")

                implementation("ch.qos.logback:logback-classic:$logbackVersion")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("io.ktor:ktor-server-test-host:$ktorVersion")
                implementation("org.junit.jupiter:junit-jupiter-params:5.9.1")
                implementation("io.kotest:kotest-assertions-core:5.5.4")
                implementation("io.mockk:mockk:1.13.3")
            }
        }
        val jsMain by getting
        val jsTest by getting
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "13"
}

node {
    download.set(false)
    version.set(nodeJsVersion)
    workDir.set(file("${project.projectDir}/.cache/nodejs"))
    npmWorkDir.set(file("${project.projectDir}/.cache/npm"))
    yarnWorkDir.set(file("${project.projectDir}/.cache/yarn"))
    nodeProjectDir.set(file("${project.projectDir}/src/jsMain/kotlin"))
}

tasks.npmInstall {
    nodeModulesOutputFilter {
        exclude("nonExistingFile")
    }
}

tasks.register<NpxTask>("buildAngularApp") {
    val workingDirectory = "${project.projectDir}/src/jsMain/kotlin"
    dependsOn(tasks.npmInstall)
    command.set("@angular/cli@$angularCliVersion")
    args.set(listOf("build"))
    workingDir.set(file(workingDirectory))
    inputs.files("package.json", "package-lock.json", "angular.json", "tsconfig.json", "tsconfigapp.json")
    inputs.dir("src")
    inputs.dir(fileTree("${workingDirectory}/node_modules").exclude(".cache"))
    outputs.dir("dist")
}

tasks.getByName<Jar>("jvmJar") {
    val taskName = if (project.hasProperty("isProduction")
        || project.gradle.startParameter.taskNames.contains("installDist")
    ) {
        "jsBrowserProductionWebpack"
    } else {
        "jsBrowserDevelopmentWebpack"
    }
    val webpackTask = tasks.getByName<KotlinWebpack>(taskName)
    dependsOn(webpackTask)
    from(File(webpackTask.destinationDirectory, webpackTask.outputFileName))
}

tasks.getByName<JavaExec>("run") {
    classpath(tasks.getByName<Jar>("jvmJar"))
    dependsOn(tasks.getByName<NpxTask>("buildAngularApp"))
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}