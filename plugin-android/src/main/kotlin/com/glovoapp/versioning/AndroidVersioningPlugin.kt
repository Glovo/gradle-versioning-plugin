package com.glovoapp.versioning

import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.api.AndroidBasePlugin
import com.glovoapp.versioning.SemanticVersioningPlugin.Companion.GROUP
import com.glovoapp.versioning.SemanticVersioningPlugin.Companion.ensureGradleVersion
import com.glovoapp.versioning.tasks.IncrementNumericVersionTask
import com.glovoapp.versioning.tasks.IncrementSemanticVersionTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.plugins.DslObject
import org.gradle.kotlin.dsl.*
import kotlin.reflect.KMutableProperty

class AndroidVersioningPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        ensureGradleVersion()

        val extension by lazy {
            DslObject(extensions.getByName("android"))
                .extensions.create<AndroidVersioningExtension>("versioning")
        }

        afterEvaluate {
            check(plugins.hasPlugin(AndroidBasePlugin::class.java)) {
                "${this@AndroidVersioningPlugin::class.java} requires an `android` to be applied"
            }
        }

        plugins.withType<AndroidBasePlugin> {
            @Suppress("UNUSED_EXPRESSION")
            extension // early init so the extension will be available for scripting under `android.versioning`

            extensions
                .getByName<AndroidComponentsExtension<*, *, *>>("androidComponents")
                .finalizeDsl {
                    val androidVersion = extension.version.get()

                    val incrementVersionCodeTask = androidVersion.code?.let {
                        tasks.register<IncrementNumericVersionTask>("incrementVersionCode") {
                            group = GROUP
                            description = "Increments the project's versionCode by given `amount`"
                            version.value(it).disallowChanges()
                        }
                    }

                    val incrementVersionNameTask = androidVersion.name?.let {
                        tasks.register<IncrementSemanticVersionTask>("incrementVersionName") {
                            group = GROUP
                            description =
                                "Increments the project's versionName by given `increment`"
                            version.value(it).disallowChanges()
                        }
                    }

                    configure<BaseExtension> {
                        defaultConfig {
                            fun <Type : Any, PropType> PersistedVersion<Type>.bind(
                                property: KMutableProperty<PropType?>,
                                transform: (Type) -> PropType
                            ) {
                                val propValue = transform(value)

                                property.setter.call(propValue)

                                onChanged {
                                    val current = property.getter.call()

                                    if (propValue != current) {
                                        error("""
                                        $name has changed but not reflected into Android build:
                                        before: $current
                                        after: $propValue
                                        """.trimIndent())
                                    }
                                }
                            }

                            androidVersion.code?.bind(::versionCode) { it }
                            androidVersion.name?.bind(::versionName) { it.toString() }
                        }
                    }

                    // ensures these tasks are run (if present in the graph) before any Android build task
                    tasks.named("preBuild") {
                        shouldRunAfter(
                            *listOfNotNull(
                                incrementVersionCodeTask,
                                incrementVersionNameTask
                            ).toTypedArray()
                        )
                    }

                    gradle.projectsEvaluated {
                        checkNotNull(androidVersion.code ?: androidVersion.name) {
                            "Please provide at least one of '${extension.versionCodeProperty.get()}' or " +
                                    "'${extension.versionCodeProperty.get()}' properties on " +
                                    "`${extension.propertiesFile.get()}`"
                        }
                    }
                }
        }
    }
}
