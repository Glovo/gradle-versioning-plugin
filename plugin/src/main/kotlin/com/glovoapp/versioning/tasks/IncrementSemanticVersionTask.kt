package com.glovoapp.versioning.tasks

import com.glovoapp.versioning.PersistedVersion
import com.glovoapp.versioning.SemanticVersion
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.gradle.kotlin.dsl.listProperty
import org.gradle.kotlin.dsl.property

open class IncrementSemanticVersionTask : DefaultTask() {

    @Internal
    val version = project.objects.property<PersistedVersion<SemanticVersion>>()

    @Input
    @Option(option = "increment", description = "The increment type")
    val increment = project.objects.property<SemanticVersion.Increment>().convention(SemanticVersion.Increment.PATCH)

    @Input
    val amount = project.objects.property<Int>().convention(1)

    @Input
    @Option(option = "preserve-pre-release", description = "Add or replace current pre-release labels")
    val preservePreRelease = project.objects.property<Boolean>().convention(false)

    @Input
    @Option(option = "preserve-build", description = "Add or replace current build labels")
    val preserveBuild = project.objects.property<Boolean>().convention(false)

    @Input
    val preReleaseLabels = project.objects.listProperty<String>()

    @Input
    val buildLabels = project.objects.listProperty<String>()

    @Option(option = "major", description = "Increments major version")
    fun major() {
        this.increment.set(SemanticVersion.Increment.MAJOR)
    }

    @Option(option = "minor", description = "Increments minor version")
    fun minor() {
        this.increment.set(SemanticVersion.Increment.MINOR)
    }

    @Option(option = "patch", description = "Increments patch version")
    fun patch() {
        this.increment.set(SemanticVersion.Increment.PATCH)
    }

    @Option(option = "amount", description = "The increment amount")
    fun amount(amount: String) {
        this.amount.set(checkNotNull(amount.toIntOrNull()?.takeIf { it >= 0 }) { "amount `$amount` must be >= 0" })
    }

    @Option(option = "preserve", description = "Add or replace current pre-release and build labels")
    fun preserve() {
        this.preservePreRelease.set(true)
        this.preserveBuild.set(true)
    }

    @Option(option = "pre-release", description = "Adds pre-release labels to the version")
    fun preReleaseLabels(labels: List<String>) {
        preReleaseLabels.set(labels)
    }

    @Option(
        option = "release",
        description = "Converts the current version into a release, by removing all `pre-release` tags"
    )
    fun release() {
        preReleaseLabels.set(emptyList())
        preservePreRelease.set(false)
    }

    @Option(option = "build", description = "Adds build labels to the version")
    fun buildLabels(labels: List<String>) {
        buildLabels.set(labels)
    }

    @TaskAction
    fun run() = with(version.get()) {
        fun List<String>.ifPreserved() = if (preservePreRelease.get()) this else emptyList()
        val prLabels = value.preReleaseIdentifiers.ifPreserved() + preReleaseLabels.get()
        val bLabels = value.buildIdentifiers.ifPreserved() + buildLabels.get()

        value = (value + increment.get() * amount.get()).copy(
            preRelease = prLabels.joinToString(separator = "."),
            build = bLabels.joinToString(separator = ".")
        )
    }

}
