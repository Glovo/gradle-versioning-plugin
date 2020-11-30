buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("org.jfrog.buildinfo:build-info-extractor-gradle:4.15.2")
    }
}

apply(from = "gradle/publish.gradle")
