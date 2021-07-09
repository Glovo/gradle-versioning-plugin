package com.glovoapp.versioning.tasks

import com.glovoapp.versioning.PersistedVersion
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.gradle.kotlin.dsl.property

open class IncrementNumericVersionTask : DefaultTask() {

    @Internal
    val version = project.objects.property<PersistedVersion<Int>>()

    @Input
    @Option(option = "amount", description = "The amount to increment the numeric version")
    val amount = project.objects.property<Int>().convention(1)

    @TaskAction
    fun run() {
        version.get().value += amount.get()
    }

}
