package com.glovoapp.versioning

import com.glovoapp.versioning.tasks.IncrementSemanticVersionTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.register

class SemanticVersioningPlugin : Plugin<Any> {

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
        gradle.rootProject { apply<SemanticVersioningPlugin>() }
    }

    private fun Project.apply() {
        val persistedProperties = rootProject.plugins.apply(PersistedVersionPlugin::class.java).persistedProperties

        val semanticVersion = persistedProperties.semanticVersion(key = "version")
        allprojects { version = semanticVersion }

        tasks.register<IncrementSemanticVersionTask>(TASK_NAME) {
            version = semanticVersion
            group = GROUP
            description = "Increments the project's semantic version by 1"
        }
    }

}
