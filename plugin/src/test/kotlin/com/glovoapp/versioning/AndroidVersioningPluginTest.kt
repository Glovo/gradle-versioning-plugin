package com.glovoapp.versioning

import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class AndroidVersioningPluginTest {

    @JvmField
    @RegisterExtension
    val gradle = GradleBuildExtension {
        buildFile("com.android.application", "com.glovoapp.android-versioning") {
            """
            android {
                compileSdkVersion(28)
            }
            """.trimIndent()
        }
    }

    @Test
    fun incrementVersionCode() = with(gradle) {
        versionFile("versionCode" to "14")

        val result = runner.withArguments("-s", "incrementVersionCode").build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":incrementVersionCode")?.outcome)
        assertTrue("versionCode=15" in versionFile.readLines())
    }

}
