package com.glovoapp.versioning

import com.glovoapp.gradle.plugin.PLUGIN_ARTIFACT
import com.glovoapp.versioning.tasks.IncrementSemanticVersionTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.register

class SemanticVersioningPlugin : Plugin<Project> {

    companion object {
        const val GROUP = "versioning"
        const val TASK_NAME = "incrementSemanticVersion"
    }

    override fun apply(target: Project): Unit = with(target) {
        if (name == "buildSrc") {
            apply(plugin = "java")

            dependencies {
                "implementation"(PLUGIN_ARTIFACT)
            }

        } else {
            val persistedProperties = plugins.apply(PersistedVersionPlugin::class.java).persistedProperties
            val semanticVersion = persistedProperties.semanticVersion(key = "version")

            allprojects { version = semanticVersion }

            tasks.register<IncrementSemanticVersionTask>(TASK_NAME) {
                group = GROUP
                description = "Increments the project's semantic version by 1"
                version.value(semanticVersion).disallowChanges()
            }

            enableExperimentalVersionSupport(semanticVersion)
        }
    }

}
