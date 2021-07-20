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
    val amount = project.objects.property<Int>().convention(1)

    @Option(option = "amount", description = "The amount to increment the numeric version")
    fun amount(amount: String) {
        this.amount.set(checkNotNull(amount.toIntOrNull()?.takeIf { it >= 0 }) { "amount `$amount` must be >= 0" })
    }

    @TaskAction
    fun run() {
        version.get().value += amount.get()
    }

}
