package com.glovoapp.versioning

import com.glovoapp.versioning.tasks.IncrementSemanticVersionTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register

class SemanticVersioningPlugin : Plugin<Project> {

    lateinit var persistedProperties: PersistedProperties

    companion object {
        const val GROUP = "versioning"
    }

    override fun apply(target: Project): Unit = with(target) {
        persistedProperties = PersistedProperties(file("$rootDir/version.properties"))

        val semanticVersion = persistedProperties.semanticVersion(key = "version")

        version = semanticVersion

        tasks.register<IncrementSemanticVersionTask>("incrementSemanticVersion") {
            group = GROUP
            description = "Increments the project's semantic version by 1"
            version = semanticVersion
        }
    }

}
