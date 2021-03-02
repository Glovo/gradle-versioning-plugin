package com.glovoapp.versioning

import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class SemanticVersioningPluginTest() {

    @JvmField
    @RegisterExtension
    val gradle = GradleBuildExtension {
        buildFile("java", "com.glovoapp.semantic-versioning")
    }

    @ParameterizedTest
    @EnumSource(SemanticVersion.Increment::class)
    fun incrementSemanticVersion(increment: SemanticVersion.Increment?) = with(gradle) {
        versionFile("version" to "1.2.3")

        val taskParam = increment?.let { "--${it.name.toLowerCase()}" }
        val expectedVersion = when (increment) {
            SemanticVersion.Increment.MAJOR -> "2.0.0"
            SemanticVersion.Increment.MINOR -> "1.3.0"
            SemanticVersion.Increment.PATCH, null -> "1.2.4"
        }

        val result = runner
            .withArguments(listOfNotNull("-s", "incrementSemanticVersion", taskParam))
            .build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":incrementSemanticVersion")?.outcome)
        assertTrue("version=$expectedVersion" in versionFile.readLines())
    }

    @Test
    fun incrementSemanticVersion_default() =
        incrementSemanticVersion(null)

}
