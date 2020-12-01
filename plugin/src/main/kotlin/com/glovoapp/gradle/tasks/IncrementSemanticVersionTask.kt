package com.glovoapp.gradle.tasks

import com.glovoapp.gradle.PersistedVersion
import com.glovoapp.gradle.SemanticVersion
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.options.Option
import java.util.*

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
