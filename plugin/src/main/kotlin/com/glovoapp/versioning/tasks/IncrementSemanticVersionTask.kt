package com.glovoapp.versioning.tasks

import com.glovoapp.versioning.PersistedVersion
import com.glovoapp.versioning.SemanticVersion
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import org.gradle.api.tasks.options.Option

open class IncrementSemanticVersionTask : DefaultTask() {

    @Internal
    lateinit var version: PersistedVersion<SemanticVersion>

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
    fun run() {
        version.value += increment
    }

}
