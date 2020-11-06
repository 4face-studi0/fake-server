import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
}
group = "studio.forface"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://dl.bintray.com/4face/4face")
}
dependencies {
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
    testImplementation("studio.forface:assert4k:0.5.7")
}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}
