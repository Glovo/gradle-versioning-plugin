import java.util.*

plugins {
    id("kotlin-library")
    `java-gradle-plugin`
    id("com.gradle.plugin-publish")
    id("plugins-portal-publish")
}

tasks.pluginDescriptors.configure {
    doLast {
        declarations.get().forEach {
            outputDirectory.file("${it.name}.properties").get().asFile.writer().use { out ->
                Properties().apply { put("implementation-class", it.implementationClass) }.store(out, null)
            }
        }
    }
}
