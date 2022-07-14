package com.glovoapp.versioning

import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.of
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.util.stream.Stream

class SemanticVersioningPluginTest {

    @JvmField
    @RegisterExtension
    val gradle = GradleBuildExtension {
        runner.withGradleVersion(SemanticVersioningPlugin.minimumGradleVersion.toString())

        buildFile {
            """
            plugins {
                java 
                id("com.glovoapp.semantic-versioning")
            }
            """.trimIndent()
        }
    }

    @ParameterizedTest
    @MethodSource("incrementParams")
    fun incrementSemanticVersion(
        currentVersion: String,
        taskParams: Array<String>,
        expectedVersion: String
    ) = with(gradle) {
        versionFile("version" to currentVersion)

        val result = runner
            .withArguments(listOfNotNull("-s", "incrementSemanticVersion", *taskParams))
            .build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":incrementSemanticVersion")?.outcome)
        assertEquals("version=$expectedVersion", versionFile.readLines().drop(1).firstOrNull())
    }

    @Test
    fun projectVersionIsSet() {
        with(gradle) {
            versionFile("version" to "1.2.3")

            buildFile.appendText("""
                
                task("storeVersion") {
                    val initial = project.version.toString()
                    doLast {
                        file("${'$'}buildDir/versions.properties")
                            .apply { parentFile.mkdirs() } 
                            .writeText("initial=${'$'}initial\nexecution=${'$'}{project.version}")
                    }
                }
                
            """.trimIndent())

            val result = runner
                .withArguments(listOfNotNull("-s", "storeVersion"))
                .build()

            assertEquals(TaskOutcome.SUCCESS, result.task(":storeVersion")?.outcome)
            assertEquals("initial=1.2.3\nexecution=1.2.3", File(root, "build/versions.properties").readText())
        }
    }

    companion object {

        @JvmStatic
        fun incrementParams() = Stream.of(
            of("1.2.3", emptyArray<String>(), "1.2.4"),
            of("1.2.3", arrayOf("--patch"), "1.2.4"),
            of("1.2.3", arrayOf("--minor"), "1.3.0"),
            of("1.2.3", arrayOf("--major"), "2.0.0"),
            of("4.0.0", arrayOf("--amount=3"), "4.0.3"),
            of("4.0.0", arrayOf("--increment=patch", "--amount=3"), "4.0.3"),
            of("4.0.0", arrayOf("--increment=minor", "--amount=3"), "4.3.0"),
            of("4.0.0", arrayOf("--increment=major", "--amount=3"), "7.0.0"),
            of("4.0.0", arrayOf("--increment=PATCH", "--amount=3"), "4.0.3"),
            of("4.0.0", arrayOf("--increment=MINOR", "--amount=3"), "4.3.0"),
            of("4.0.0", arrayOf("--increment=MAJOR", "--amount=3"), "7.0.0"),

            of("1.2.3", arrayOf("--amount=0", "--pre-release=abc"), "1.2.3-abc"),
            of("1.2.3", arrayOf("--pre-release=abc"), "1.2.4-abc"),
            of("1.2.3-abc", arrayOf("--pre-release=abc"), "1.2.4-abc"),
            of("1.2.3+def", arrayOf("--pre-release=abc"), "1.2.4-abc"),
            of("1.2.3-abc", arrayOf("--pre-release=def.123"), "1.2.4-def.123"),
            of("1.2.3-abc+def", arrayOf("--pre-release=123.456", "--pre-release=789"), "1.2.4-123.456.789"),

            of("1.2.3", arrayOf("--preserve", "--amount=0", "--pre-release=abc"), "1.2.3-abc"),
            of("1.2.3", arrayOf("--preserve", "--pre-release=abc"), "1.2.4-abc"),
            of("1.2.3-abc", arrayOf("--preserve", "--pre-release=abc"), "1.2.4-abc.abc"),
            of("1.2.3+def", arrayOf("--preserve", "--pre-release=abc"), "1.2.4-abc+def"),
            of("1.2.3-abc", arrayOf("--preserve", "--pre-release=def.123"), "1.2.4-abc.def.123"),
            of(
                "1.2.3-abc+def",
                arrayOf("--preserve", "--pre-release=123.456", "--pre-release=789"),
                "1.2.4-abc.123.456.789+def"
            ),

            of("1.2.3", arrayOf("--amount=0", "--build=abc"), "1.2.3+abc"),
            of("1.2.3", arrayOf("--build=abc"), "1.2.4+abc"),
            of("1.2.3-abc", arrayOf("--build=abc"), "1.2.4+abc"),
            of("1.2.3+def", arrayOf("--build=abc"), "1.2.4+abc"),
            of("1.2.3-abc", arrayOf("--build=def.123"), "1.2.4+def.123"),
            of("1.2.3-abc+def", arrayOf("--build=123.456", "--build=789"), "1.2.4+123.456.789"),

            of("1.2.3", arrayOf("--preserve", "--amount=0", "--build=abc"), "1.2.3+abc"),
            of("1.2.3", arrayOf("--preserve", "--build=abc"), "1.2.4+abc"),
            of("1.2.3-abc", arrayOf("--preserve", "--build=abc"), "1.2.4-abc+abc"),
            of("1.2.3+def", arrayOf("--preserve", "--build=abc"), "1.2.4+def.abc"),
            of("1.2.3-abc", arrayOf("--preserve", "--build=def.123"), "1.2.4-abc+def.123"),
            of("1.2.3-abc+def", arrayOf("--preserve", "--build=123.456", "--build=789"), "1.2.4-abc+def.123.456.789")
        )

    }

}
