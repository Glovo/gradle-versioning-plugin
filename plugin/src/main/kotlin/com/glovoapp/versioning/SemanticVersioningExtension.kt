package com.glovoapp.versioning

import org.gradle.api.Project
import org.gradle.kotlin.dsl.property

open class SemanticVersioningExtension(
    project: Project
) : BaseExtension(project) {

    val versionProperty = project.objects.property<String>()
        .apply { finalizeValueOnRead() }
        .convention("version")

    val version = project.objects.property<PersistedVersion<SemanticVersion>>()
        .value(properties.zip(versionProperty) { props, key -> props.semanticVersion(key = key) })
        .apply { finalizeValueOnRead() }
        .forUseAtConfigurationTime()

    val experimentalSupport = project.objects.property<Boolean>()
        .apply { finalizeValueOnRead() }
        .convention(true)

}
