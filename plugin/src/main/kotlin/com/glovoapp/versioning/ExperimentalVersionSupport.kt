package com.glovoapp.versioning

import com.glovoapp.versioning.tasks.EnsureExperimentalVersionTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.publish.maven.tasks.PublishToMavenLocal
import org.gradle.configurationcache.extensions.serviceOf
import org.gradle.internal.logging.text.StyledTextOutput
import org.gradle.internal.logging.text.StyledTextOutputFactory
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import java.io.File

private const val PROPERTY_NAME = "com.glovoapp.versioning.experimental"
private const val REPORT_DIR = "m2publications"

private val Task.allDependencies: Sequence<Task>
    get() = with(project.gradle.taskGraph) {
        when (hasTask(this@allDependencies)) {
            true -> getDependencies(this@allDependencies)
                .asSequence()
                .flatMap { sequenceOf(it) + it.allDependencies }
            false -> emptySequence() // in case of the task was excluded to run
        }
    }

private val Task.styledOutput
    get() = project.serviceOf<StyledTextOutputFactory>().create(this::class.java)

/**
 * Configures the build to:
 * 1. Updates the version with a `dev-experimental-TIMESTAMP` pre-release label if it's publishing to `mavenLocal`
 * 2. Generates a list of published artifacts at the end of the build to facility its local consumption
 */
fun Project.enableExperimentalVersionSupport(semanticVersion: PersistedVersion<SemanticVersion>): Unit = with(project) {
    check(this == rootProject) { "${::enableExperimentalVersionSupport.name} must be applied from root project" }

    // updates the current version into an experimental one, it should be the first task to run
    val versionTask = tasks.register<EnsureExperimentalVersionTask>("experimentalVersion") {
        group = SemanticVersioningPlugin.GROUP
        description = "Modifies the project version by adding a 'dev-experimental' pre-release tag"
        version.value(semanticVersion).disallowChanges()

        onlyIf { project.findProperty(PROPERTY_NAME)?.toString()?.toBoolean() ?: true }
        onlyIf { semanticVersion.value.preRelease == null }

        doLast {
            with(styledOutput) {
                val description = withStyle(StyledTextOutput.Style.Description)
                val identifier = withStyle(StyledTextOutput.Style.Identifier)

                text("To honor Maven's snapshot policy, the project's version has been changed to ")
                identifier.text(semanticVersion)
                text(" because it's published to ")
                description.println("mavenLocal()")
                text("You can disable this behavior by adding `")
                identifier.text(PROPERTY_NAME)
                text("` to your `")
                identifier.text("gradle.properties")
                text("` or by adding `")
                identifier.text("-x ${this@register.name}")
                println("` to the command line")
            }
        }
    }

    // collects all publications done to MavenLocal in a file (to support included builds)
    val collectPublications = tasks.register("collectMavenLocalPublications") {
        val file = file("$buildDir/$REPORT_DIR/${project.name}.txt")

        outputs.file(file)
        outputs.upToDateWhen { false }
        doLast {
            file.writeText(
                allDependencies
                    .distinct()
                    .mapNotNull { (it as? PublishToMavenLocal)?.publication }
                    .map { "${it.groupId}:${it.artifactId}:${it.version}" }
                    .joinToString(separator = "\n"))
        }
    }

    // prints to the console all publications done to MavenLocal
    val reportPublications = tasks.register("reportMavenLocalPublications") {
        val reports = fileTree(rootDir) {
            // this allows to scan this build and also any included build
            include("**/${buildDir.name}/$REPORT_DIR/*.txt")
        }

        inputs.files(reports)
        dependsOn(collectPublications)
        doLast {
            with(styledOutput) {
                val description = withStyle(StyledTextOutput.Style.Description)
                val identifier = withStyle(StyledTextOutput.Style.Identifier)
                val info = withStyle(StyledTextOutput.Style.Info)

                text("The following artifacts are available at ")
                description.text("~/.m2/")
                println(":")

                fun File.printPublications() = readLines().sorted().forEach {
                    val (group, artifact, version) = it.split(":")

                    text(" - ")
                    identifier.text(group)
                    text(":$artifact:")
                    info.println(version)
                }

                val builds = reports.sorted()
                when (builds.size) {
                    1 -> builds.first().printPublications()
                    else -> builds.forEach {
                        println()
                        text("From ")
                        description.text(it.nameWithoutExtension)
                        println(":")
                        it.printPublications()
                        println()
                    }
                }
            }
        }
    }

    allprojects {

        // makes versionTask the first task to run
        tasks.configureEach {
            if (this != versionTask.get()) {
                mustRunAfter(versionTask)
            }
        }

        plugins.withType<MavenPublishPlugin> {
            val mavenLocal = tasks.named(MavenPublishPlugin.PUBLISH_LOCAL_LIFECYCLE_TASK_NAME) {
                dependsOn(versionTask)
                finalizedBy(reportPublications)
            }
            collectPublications.configure { dependsOn(mavenLocal) }
        }
    }
}

