package com.glovoapp.versioning

import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.property

open class SemanticVersioningExtension(
    project: Project
) : BaseExtension(project) {

    val versionProperty: Property<String> = project.objects.property<String>()
        .apply { finalizeValueOnRead() }
        .convention("version")

    val version: Provider<PersistedVersion<SemanticVersion>> = properties
        .zip(versionProperty) { props, key -> props.semanticVersion(key = key) }
        .forUseAtConfigurationTime()

    val experimentalSupport : Property<Boolean> = project.objects.property<Boolean>()
        .convention(true)
        .apply { finalizeValueOnRead() }

}
