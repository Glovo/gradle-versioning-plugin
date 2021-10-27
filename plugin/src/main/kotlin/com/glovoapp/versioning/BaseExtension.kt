package com.glovoapp.versioning

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import javax.inject.Inject

abstract class BaseExtension @Inject constructor(
    private val project: Project
) {

    val propertiesFile = project.objects.fileProperty()
        .apply { finalizeValueOnRead() }
        .convention(project.layout.projectDirectory.file("version.properties"))

    val properties = propertiesFile
        .mapCaching { PersistedProperties(it.asFile.absoluteFile) }
        .forUseAtConfigurationTime()

    private fun <In, Out> Provider<In>.mapCaching(transform: (In) -> Out): Provider<Out> =
        project.provider(lazy { transform(get()) }::value)

}
