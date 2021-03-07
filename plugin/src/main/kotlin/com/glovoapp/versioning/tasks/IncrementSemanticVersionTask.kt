package com.glovoapp.versioning.tasks

import com.glovoapp.versioning.PersistedVersion
import com.glovoapp.versioning.SemanticVersion
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

open class IncrementSemanticVersionTask : DefaultTask() {

    @Internal
    lateinit var version: PersistedVersion<SemanticVersion>

    @Input
    @Option(option = "increment", description = "The increment type")
    var increment = SemanticVersion.Increment.PATCH

    @Input
    var amount = 1

    @Input
    @Option(option = "preserve-pre-release", description = "Add or replace current pre-release labels")
    var preservePreRelease = false

    @Input
    @Option(option = "preserve-build", description = "Add or replace current build labels")
    var preserveBuild = false

    @Input
    @Option(option = "pre-release", description = "Adds pre-release labels to the version")
    var preReleaseLabels = mutableListOf<String>()

    @Input
    @Option(option = "build", description = "Adds build labels to the version")
    var buildLabels = mutableListOf<String>()

    @Option(option = "major", description = "Increments major version")
    fun major() {
        this.increment = SemanticVersion.Increment.MAJOR
    }

    @Option(option = "minor", description = "Increments minor version")
    fun minor() {
        this.increment = SemanticVersion.Increment.MINOR
    }

    @Option(option = "patch", description = "Increments patch version")
    fun patch() {
        this.increment = SemanticVersion.Increment.PATCH
    }

    @Option(option = "amount", description = "The increment amount")
    fun amount(amount: String) {
        this.amount = checkNotNull(amount.toIntOrNull()?.takeIf { it >= 0 }) { "amount `$amount` must be >= 0" }
    }

    @Input
    @Option(option = "preserve", description = "Add or replace current pre-release and build labels")
    fun preserve() {
        this.preservePreRelease = true
        this.preserveBuild = true
    }

    @TaskAction
    fun run() = with(version) {
        val prLabels = if (preservePreRelease) value.preReleaseIdentifiers + preReleaseLabels else preReleaseLabels
        val bLabels = if (preservePreRelease) value.buildIdentifiers + buildLabels else buildLabels

        value = (value + increment * amount).copy(
            preRelease = prLabels.joinToString(separator = "."),
            build = bLabels.joinToString(separator = ".")
        )
    }

}
