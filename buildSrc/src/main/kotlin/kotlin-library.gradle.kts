import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    kotlin("jvm")
}

val javaTarget = JavaVersion.VERSION_1_8

java {
    sourceCompatibility = javaTarget
    targetCompatibility = javaTarget
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = javaTarget.toString()
}
