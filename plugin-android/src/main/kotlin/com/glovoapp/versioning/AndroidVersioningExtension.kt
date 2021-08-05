package com.glovoapp.versioning

import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.property

open class AndroidVersioningExtension(
    project: Project
) : BaseExtension(project) {

    val versionCodeProperty: Property<String> = project.objects.property<String>()
        .apply { finalizeValueOnRead() }
        .convention("versionCode")

    val versionNameProperty: Property<String> = project.objects.property<String>()
        .apply { finalizeValueOnRead() }
        .convention("versionName")

    val version: Provider<AndroidVersion> = properties
        .zip(versionCodeProperty, ::Pair)
        .zip(versionNameProperty) { (props, codeKey), nameKey ->
            AndroidVersion(
                code = if (codeKey in props.keys)
                    props.numericVersion(key = codeKey) else null,
                name = if (nameKey in props.keys)
                    props.semanticVersion(key = nameKey) else null
            )
        }
        .forUseAtConfigurationTime()

}
