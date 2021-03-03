package com.glovoapp.versioning

import com.glovoapp.versioning.tasks.IncrementSemanticVersionTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.register

class SemanticVersioningPlugin : Plugin<Any> {

    lateinit var persistedProperties: PersistedProperties

    companion object {
        const val GROUP = "versioning"
        const val TASK_NAME = "incrementSemanticVersion"
    }

    override fun apply(target: Any) = when (target) {
        is Settings -> target.apply()
        is Project -> target.apply()
        else -> error("Unsupported target $target")
    }

    private fun Settings.apply() {
        gradle.allprojects { apply<SemanticVersioningPlugin>() }
    }

    private fun Project.apply() {
        persistedProperties = when (this) {
            rootProject -> PersistedProperties(file("$rootDir/version.properties"))
            else -> rootProject.plugins.apply(SemanticVersioningPlugin::class.java).persistedProperties
        }

        val semanticVersion = persistedProperties.semanticVersion(key = "version")

        version = semanticVersion

        when (this) {
            rootProject -> tasks.register<IncrementSemanticVersionTask>(TASK_NAME) { version = semanticVersion }
            else -> tasks.register(TASK_NAME) { dependsOn(rootProject.tasks.named(TASK_NAME)) }
        }.configure {
            group = GROUP
            description = "Increments the project's semantic version by 1"
        }
    }

}
