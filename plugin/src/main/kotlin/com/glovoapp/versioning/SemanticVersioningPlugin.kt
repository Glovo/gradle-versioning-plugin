package com.glovoapp.versioning

import com.glovoapp.versioning.SemanticVersion.Companion.toVersion
import com.glovoapp.versioning.tasks.IncrementSemanticVersionTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

class SemanticVersioningPlugin : Plugin<Project> {

    companion object {
        const val GROUP = "versioning"
        const val TASK_NAME = "incrementSemanticVersion"
        val minimumGradleVersion = SemanticVersion(7, 2)

        fun Project.ensureGradleVersion() = check(gradle.gradleVersion.toVersion() >= minimumGradleVersion) {
            "This plugin requires at least Gradle $minimumGradleVersion"
        }

    }

    override fun apply(target: Project): Unit = with(target) {
        ensureGradleVersion()

        val extension = extensions.create<SemanticVersioningExtension>("semanticVersion")

        allprojects {
            version = ToStringProviderWrapper(extension.version)
        }

        tasks.register<IncrementSemanticVersionTask>(TASK_NAME) {
            group = GROUP
            description = "Increments the project's semantic version by given `increment`"
            version.value(extension.version).disallowChanges()
        }

        afterEvaluate {
            if (extension.experimentalSupport.get()) {
                apply<ExperimentalVersionPlugin>()
            }
        }
    }

}

