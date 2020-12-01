package com.glovoapp.versioning.tasks

import com.glovoapp.versioning.PersistedVersion
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

open class IncrementNumericVersionTask : DefaultTask() {

    @Internal
    lateinit var version: PersistedVersion<Int>

    @Input
    @Option(option = "amount", description = "The amount to increment the numeric version")
    var amount = 1

    @TaskAction
    fun run() {
        version.value += amount
    }

}
