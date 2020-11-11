package com.glovoapp.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

@SuppressWarnings("UnstableApiUsage")
open class IncrementPersistedSemanticVersionTask() : DefaultTask() {

    init {
        group = "release"
    }

    @get:Input
    var semanticVersion = PersistedSemanticVersion("version.properties")

    private var increment = SemanticVersion.Increment.PATCH

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
    fun run() {
        semanticVersion.apply(increment)
    }
}
