package com.glovoapp.versioning

import org.gradle.api.Project
import org.gradle.kotlin.dsl.property

open class AndroidVersioningExtension(
    project: Project
) : BaseExtension(project) {

    val versionCodeProperty = project.objects.property<String>()
        .apply { finalizeValueOnRead() }
        .convention("versionCode")

    val versionNameProperty = project.objects.property<String>()
        .apply { finalizeValueOnRead() }
        .convention("versionName")

    val version = project.objects.property<AndroidVersion>()
        .value( properties
            .zip(versionCodeProperty, ::Pair)
            .zip(versionNameProperty) { (props, codeKey), nameKey ->
                AndroidVersion(
                    code = if (codeKey in props.keys)
                        props.numericVersion(key = codeKey) else null,
                    name = if (nameKey in props.keys)
                        props.semanticVersion(key = nameKey) else null
                )
        })
        .apply { finalizeValueOnRead() }
        .forUseAtConfigurationTime()

}
