package com.glovoapp.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.options.Option
import java.io.File
import java.util.*

@Suppress("LeakingThis")
open class IncrementPersistedSemanticVersionTask() : DefaultTask() {

    init {
        group = "release"
        onlyIf { version != null }
    }

    @get:Nested
    @get:Optional
    val version
        get() = project.version as? PersistedSemanticVersion

    @Input
    var increment = SemanticVersion.Increment.PATCH

    @Option(option = "major", description = "Increments major version")
    fun major() {
        increment = SemanticVersion.Increment.MAJOR
    }

    @Option(option = "minor", description = "Increments minor version")
    fun minor() {
        increment = SemanticVersion.Increment.MINOR
    }

    @Option(option = "patch", description = "Increments patch version")
    fun patch() {
        increment = SemanticVersion.Increment.PATCH
    }

    @TaskAction
    fun run() = with(version!!) {
        value += increment
        flush()
    }

}
