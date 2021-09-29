package com.glovoapp.versioning

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.invocation.Gradle
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.apply
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

abstract class BaseExtension @Inject constructor(
    project: Project
) {

    /**
     * A cache for [PersistedProperties] "per build", which is not the same as having a static JVM field because it
     * will be cross-builds.
     */
    private val cache = generateSequence(project.gradle, Gradle::getParent)
        .last()
        .plugins
        .apply(PersistedPropertiesCachePlugin::class)
        .cache

    val propertiesFile: RegularFileProperty = project.objects.fileProperty()
        .apply { finalizeValueOnRead() }
        .convention(project.layout.projectDirectory.file("version.properties"))

    val properties: Provider<PersistedProperties> = propertiesFile
        .map { it.asFile.absoluteFile }
        .map { cache.computeIfAbsent(it, ::PersistedProperties) }
        .forUseAtConfigurationTime()

    internal class PersistedPropertiesCachePlugin : Plugin<Gradle> {

        val cache = ConcurrentHashMap<File, PersistedProperties>()

        override fun apply(target: Gradle) {
        }

    }

}
