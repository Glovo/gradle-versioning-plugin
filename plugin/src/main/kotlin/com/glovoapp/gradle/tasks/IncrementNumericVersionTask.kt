package com.glovoapp.gradle.tasks

import com.glovoapp.gradle.PersistedVersion
import com.glovoapp.gradle.SemanticVersion
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.options.Option
import java.util.*

open class IncrementNumericVersionTask : DefaultTask() {

    @Internal
    lateinit var version: PersistedVersion<Int>

    @Option(option = "amount", description = "The amount to increment the numeric version")
    var amount = 1

    @TaskAction
    fun run() {
        version.value += amount
    }

}
