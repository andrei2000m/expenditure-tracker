buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
        classpath("com.android.tools.build:gradle:7.4.2")
    }
}

group = "me.andrei2000m"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
    }
}

// TODO: See if any of the below should be kept
//
//plugins {
//    kotlin("multiplatform") version "1.8.10"
//    kotlin("plugin.serialization") version "1.8.10"
//    id("com.android.application") version "7.3.0" apply false
//    kotlin("android") version "1.5.31" apply false
//    id("com.github.node-gradle.node") version "3.5.1"
//    application
//}
//
//val nodeJsVersion = "16.14.0"
//val angularCliVersion = "15.1.3"
//
//tasks.test {
//    useJUnitPlatform()
//}
//
//tasks.withType<KotlinCompile>() {
//    kotlinOptions.jvmTarget = "13"
//}
//
//node {
//    download.set(false)
//    version.set(nodeJsVersion)
//    workDir.set(file("${project.projectDir}/.cache/nodejs"))
//    npmWorkDir.set(file("${project.projectDir}/.cache/npm"))
//    yarnWorkDir.set(file("${project.projectDir}/.cache/yarn"))
//    nodeProjectDir.set(file("${project.projectDir}/src/jsMain/kotlin"))
//}
//
//tasks.npmInstall {
//    nodeModulesOutputFilter {
//        exclude("nonExistingFile")
//    }
//}
//
//tasks.register<NpxTask>("buildAngularApp") {
//    val workingDirectory = "${project.projectDir}/src/jsMain/kotlin"
//    dependsOn(tasks.npmInstall)
//    command.set("@angular/cli@$angularCliVersion")
//    args.set(listOf("build"))
//    workingDir.set(file(workingDirectory))
//    inputs.files("package.json", "package-lock.json", "angular.json", "tsconfig.json", "tsconfigapp.json")
//    inputs.dir("src")
//    inputs.dir(fileTree("${workingDirectory}/node_modules").exclude(".cache"))
//    outputs.dir("dist")
//}
//
//tasks.getByName<Jar>("jvmJar") {
//    val taskName = if (project.hasProperty("isProduction")
//        || project.gradle.startParameter.taskNames.contains("installDist")
//    ) {
//        "jsBrowserProductionWebpack"
//    } else {
//        "jsBrowserDevelopmentWebpack"
//    }
//    val webpackTask = tasks.getByName<KotlinWebpack>(taskName)
//    dependsOn(webpackTask)
//    from(File(webpackTask.destinationDirectory, webpackTask.outputFileName))
//}
//
//tasks.getByName<JavaExec>("run") {
//    classpath(tasks.getByName<Jar>("jvmJar"))
//    dependsOn(tasks.getByName<NpxTask>("buildAngularApp"))
//}
//
//application {
//    mainClass.set("io.ktor.server.netty.EngineMain")
//}