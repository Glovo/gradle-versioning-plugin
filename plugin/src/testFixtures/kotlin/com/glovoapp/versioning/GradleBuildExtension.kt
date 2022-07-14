package com.glovoapp.versioning

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import java.io.File
import java.io.FileOutputStream
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
            .withJaCoCo()
    }

    private fun GradleRunner.withJaCoCo() = apply {
        javaClass.classLoader.getResourceAsStream("testkit-gradle.properties")!!.use { input ->
            FileOutputStream(File(projectDir, "gradle.properties"), true).use { output ->
                input.copyTo(output)
            }
        }
    }

    val buildFile by lazy { File(root, "build.gradle.kts") }

    val versionFile by lazy {
        File(root, "version.properties")
            .apply {
                writeText(
                    """
                    version=0.0.1
                    """.trimIndent()
                )
            }
    }

    override fun beforeEach(context: ExtensionContext?) {
        root.mkdirs()
        File(root, "settings.gradle.kts").createNewFile()

        versionFile.writeText(
            """
            version=0.0.1
            versionCode=10
            """.trimIndent()
        )

        setup?.invoke(this)
    }

    operator fun File.invoke(content: () -> String) {
        buildFile.writeText(content())
    }

    fun versionFile(vararg entries: Pair<String, String>) =
        versionFile.versionFile(*entries)

    fun File.versionFile(vararg entries: Pair<String, String>) = with(Properties()) {
        this@versionFile.takeIf { it.isFile }?.reader()?.use(::load)
        putAll(entries)
        writer().use { store(it, null) }
    }

}
