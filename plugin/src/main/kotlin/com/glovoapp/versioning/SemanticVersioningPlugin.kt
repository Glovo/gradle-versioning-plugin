package com.glovoapp.versioning

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
        val minimumGradleVersion = SemanticVersion(7, 1, 1)

        fun Project.ensureGradleVersion() =
            check(SemanticVersion.parse(gradle.gradleVersion) >= minimumGradleVersion) {
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
            description = "Increments the project's semantic version by 1"
            version.value(extension.version).disallowChanges()
        }

        afterEvaluate {
            if (extension.experimentalSupport.get()) {
                apply<ExperimentalVersionPlugin>()
            }
        }
    }

}

