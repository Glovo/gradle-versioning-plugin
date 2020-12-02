package com.glovoapp.versioning

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import java.io.File
import java.util.*

class GradleBuildExtension @JvmOverloads constructor(
    private val setup: (GradleBuildExtension.() -> Unit)? = null
) : BeforeEachCallback {

    val root by lazy {
        System.getProperty("tests.tmp.dir")
            ?.let(::File)
            ?: File.createTempFile("junit", "")
    }

    val runner by lazy {
        GradleRunner.create()
            .forwardOutput()
            .withPluginClasspath()
            .withProjectDir(root)
    }

    val buildFile
        get() = File(root, "build.gradle.kts")

    val versionFile
        get() = File(root, "version.properties")

    override fun beforeEach(context: ExtensionContext?) {
        File(GradleBuildExtension::class.java.getResource("/testProject").file)
            .copyRecursively(root, overwrite = true)

        setup?.invoke(this)
    }

    fun buildFile(vararg plugins: String, content: (() -> String)? = null) {
        buildFile.writeText(
            """
            |plugins {
            ${plugins.joinToString(separator = "\n") { "|   id(\"$it\")" }}
            |}
            |
            |repositories {
            |    google()
            |    jcenter()
            |}
            |
            """.trimMargin() + (content?.invoke() ?: "")
        )
    }

    fun versionFile(vararg entries: Pair<String, String>) = with(Properties()) {
        versionFile.takeIf { it.isFile }?.reader()?.use(::load)
        putAll(entries)
        versionFile.writer().use { store(it, null) }
    }

}
