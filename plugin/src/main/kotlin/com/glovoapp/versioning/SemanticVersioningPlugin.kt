package com.glovoapp.versioning

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.BasePlugin
import com.glovoapp.versioning.tasks.IncrementSemanticVersionTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType

class SemanticVersioningPlugin : Plugin<Project> {

    internal lateinit var persistedProperties: PersistedProperties

    companion object {
        internal const val GROUP = "versioning"
    }

    override fun apply(target: Project): Unit = with(target) {
        persistedProperties = PersistedProperties(file("$rootDir/version.properties"))

        val semanticVersion = persistedProperties.semanticVersion(key = "version")

        version = semanticVersion

        val incrementVersionTask = tasks.register<IncrementSemanticVersionTask>("incrementSemanticVersion") {
            group = GROUP
            description = "Increments the project's semantic version by 1"
            version = semanticVersion
        }

        plugins.withType<BasePlugin> {

            configure<BaseExtension> {
                defaultConfig {
                    semanticVersion.onChanged { versionName = it.toString() }
                }
            }

            tasks.named("preBuild") {
                shouldRunAfter(incrementVersionTask)
            }
        }
    }

}
