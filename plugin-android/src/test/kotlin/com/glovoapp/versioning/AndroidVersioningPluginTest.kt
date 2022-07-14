package com.glovoapp.versioning

import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.internal.PluginUnderTestMetadataReading
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.io.File

abstract class AndroidVersioningPluginTest(
    gradleVersion: String,
    agpVersion: String,
) {

    class Gradle72_AGP701 : AndroidVersioningPluginTest(SemanticVersioningPlugin.minimumGradleVersion.toString(),"7.0.1")
    class Gradle742_AGP721 : AndroidVersioningPluginTest("7.4.2","7.2.1")

    @JvmField
    @RegisterExtension
    val gradle = GradleBuildExtension {
        runner.withGradleVersion(gradleVersion)

        val pluginClassPath = PluginUnderTestMetadataReading.readImplementationClasspath()
            .joinToString(separator = ", ") { "\"${it}\"" }

        File(root, "buildSrc/build.gradle.kts").apply { parentFile.mkdirs() }.writeText("""
            plugins {
                `kotlin-dsl`
            }
            repositories {
                mavenCentral()
                google()
            }
            dependencies {
                implementation("com.android.tools.build:gradle:$agpVersion")
                implementation(files($pluginClassPath))
            }
        """.trimIndent())

        buildFile {
            """
            plugins {
                id("com.android.application")
                id("com.glovoapp.android-versioning")
            }
            
            android {
                compileSdkVersion(28)
            }
            """.trimIndent()
        }

        File(root, "src/main/AndroidManifest.xml").apply {
            parentFile.mkdirs()
            writeText("<manifest package=\"test.app\"/>")
        }
    }

    @Test
    fun incrementVersionCode() = with(gradle) {
        versionFile("versionCode" to "14")

        val result = runner.withArguments("-s", "incrementVersionCode").build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":incrementVersionCode")?.outcome)
        assertTrue("versionCode=15" in versionFile.readLines())
    }

    @Test
    fun incrementVersionName() = with(gradle) {
        versionFile("versionName" to "0.1.2")

        val result = runner.withArguments("-s", "incrementVersionName").build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":incrementVersionName")?.outcome)
        assertTrue("versionName=0.1.3" in versionFile.readLines())
    }

    @Test
    fun changePropertyFile() = with(gradle) {
        buildFile.appendText(
            """
                
            android {
                versioning {
                    propertiesFile.set(file("myVersions.properties"))
                    versionCodeProperty.set("myVersionCode")
                    versionNameProperty.set("myVersionName")
                }
            }
            """.trimIndent()
        )

        val myVersionFile = File(root, "myVersions.properties").apply {
            versionFile(
                "myVersionCode" to "20",
                "myVersionName" to "3.2.1"
            )
        }

        val result = runner.withArguments("-s", "incrementVersionCode", "incrementVersionName").build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":incrementVersionCode")?.outcome)
        assertEquals(TaskOutcome.SUCCESS, result.task(":incrementVersionName")?.outcome)
        assertTrue("myVersionCode=21" in myVersionFile.readLines())
        assertTrue("myVersionName=3.2.2" in myVersionFile.readLines())
    }

}
