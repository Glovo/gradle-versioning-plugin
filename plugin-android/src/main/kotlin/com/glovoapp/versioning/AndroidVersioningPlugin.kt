package com.glovoapp.versioning

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.api.AndroidBasePlugin
import com.glovoapp.gradle.plugin_android.PLUGIN_ARTIFACT
import com.glovoapp.versioning.SemanticVersioningPlugin.Companion.GROUP
import com.glovoapp.versioning.tasks.IncrementNumericVersionTask
import com.glovoapp.versioning.tasks.IncrementSemanticVersionTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.register

class AndroidVersioningPlugin : Plugin<Project> {

    companion object {
        const val KEY_VERSION_CODE = "versionCode"
        const val KEY_VERSION_NAME = "versionName"
    }

    override fun apply(target: Project): Unit = with(target) {
        if (name == "buildSrc") {
            apply(plugin = "java")

            dependencies {
                "implementation"(PLUGIN_ARTIFACT)
            }

        } else {
            val persistedProperties = rootProject.plugins.apply(PersistedVersionPlugin::class.java).persistedProperties

            val numericVersion = if (KEY_VERSION_CODE in persistedProperties.keys)
                persistedProperties.numericVersion(key = KEY_VERSION_CODE) else null
            val semanticVersion = if (KEY_VERSION_NAME in persistedProperties.keys)
                persistedProperties.semanticVersion(key = KEY_VERSION_NAME) else null

            val incrementVersionCodeTask = numericVersion?.let {
                tasks.register<IncrementNumericVersionTask>("incrementVersionCode") {
                    group = GROUP
                    description = "Increments the project's versionCode by 1"
                    version.value(numericVersion).disallowChanges()
                }
            }

            val incrementVersionNameTask = semanticVersion?.let {
                tasks.register<IncrementSemanticVersionTask>("incrementVersionName") {
                    group = GROUP
                    description = "Increments the project's versionName by 1"
                    version.value(semanticVersion).disallowChanges()
                }
            }

            afterEvaluate {
                check(plugins.hasPlugin(AndroidBasePlugin::class.java)) {
                    "${this@AndroidVersioningPlugin::class.java} requires an `android` to be applied"
                }

                configure<BaseExtension> {
                    defaultConfig {
                        numericVersion?.onChanged { versionCode = it }
                        semanticVersion?.onChanged { versionName = it.toString() }
                    }
                }

                // ensures these task are run (if present in the graph) before any Android build task
                tasks.named("preBuild") {
                    shouldRunAfter(
                        *listOfNotNull(
                            incrementVersionCodeTask,
                            incrementVersionNameTask
                        ).toTypedArray()
                    )
                }
            }
        }
    }

}
