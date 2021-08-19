plugins {
    id("kotlin-library")
    `java-gradle-plugin`
    id("com.gradle.plugin-publish")
    id("plugins-portal-publish")
}

val outputDir = layout.buildDirectory.dir("generated/plugins/dsl")

// generates plugins DSL from the plugins descriptions, similar to `kotlin-dsl`
val generateDSL = tasks.register("generatePluginsDSL") {
    outputs.dir(outputDir)

    onlyIf { gradlePlugin.plugins.isNotEmpty() }
    doLast {
        outputDir.get().asFile.apply { deleteRecursively(); mkdirs() }
        outputDir.get().file("VersioningPluginDSL.kt").asFile.writeText(
            gradlePlugin.plugins
                .filter { it.displayName != null }
                .joinToString(separator = "\n\n", prefix = "@file:JvmMultifileClass\n\n") {
                    """
                        val org.gradle.plugin.use.PluginDependenciesSpec.`${it.displayName}`
                            get() = id("${it.id}")
                    """.trimIndent()
                }
        )
    }

    kotlin.sourceSets["main"].kotlin.srcDir(files(outputDir).builtBy(this@register))
}

gradlePlugin.plugins.all {
    this@all.description = project.description

    generateDSL.configure {
        inputs.property("plugin:$id", implementationClass)
    }
}
