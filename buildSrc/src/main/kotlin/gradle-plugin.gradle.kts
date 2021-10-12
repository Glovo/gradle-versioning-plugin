import java.util.*

plugins {
    id("kotlin-library")
    `java-gradle-plugin`
    id("com.gradle.plugin-publish")
    id("plugins-portal-publish")
}

// a POM description is required by Maven Central
gradlePlugin.plugins.all {
    this@all.description = project.description
}

tasks.pluginDescriptors.configure {
    doLast {
        declarations.get().forEach {
            outputDirectory.file("${it.name}.properties").get().asFile
                .writeText("implementation-class=${it.implementationClass}\n")
        }
    }
}
