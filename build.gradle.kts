import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlin = "1.4.10"
    kotlin("jvm") version kotlin
    kotlin("plugin.serialization") version kotlin
}
group = "studio.forface"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/4face/4face")
}
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    testImplementation(kotlin("test-junit"))
    testImplementation("studio.forface:assert4k:0.5.7")
}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}
