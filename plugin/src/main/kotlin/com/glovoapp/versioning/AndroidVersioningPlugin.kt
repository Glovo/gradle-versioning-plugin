package com.glovoapp.versioning

import com.android.build.gradle.BasePlugin
import com.android.build.gradle.BaseExtension
import com.glovoapp.versioning.SemanticVersioningPlugin.Companion.GROUP
import com.glovoapp.versioning.tasks.IncrementNumericVersionTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*

class AndroidVersioningPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        apply<SemanticVersioningPlugin>()

        val persistedProperties = the<SemanticVersioningPlugin>().persistedProperties

        plugins.withType<BasePlugin> {
            val numericVersion = persistedProperties.numericVersion(key = "versionCode")

            configure<BaseExtension> {
                defaultConfig {
                    numericVersion.onChanged { versionCode = it }
                }
            }

            val incrementVersionCodeTask = tasks.register<IncrementNumericVersionTask>("incrementVersionCode") {
                group = GROUP
                description = "Increments the project's versionCode by 1"
            }

            tasks.named("preBuild") {
                shouldRunAfter(incrementVersionCodeTask)
            }
        }
    }

}
