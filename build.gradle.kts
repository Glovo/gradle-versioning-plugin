import org.jetbrains.kotlin.gradle.dsl.KotlinSingleTargetExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.sonarqube.gradle.SonarQubeExtension.SONARQUBE_TASK_NAME
import java.lang.Thread.sleep

plugins {
    kotlin("jvm") version embeddedKotlinVersion apply false
    id("com.glovoapp.artifactory") version "0.1.16"
    id("com.glovoapp.semantic-versioning") version "0.1.23"
    id("pl.droidsonroids.jacoco.testkit") version "1.0.8" apply false
    id("org.sonarqube") version "3.3"
}

sonarqube {
    properties {
        property("sonar.projectKey", "Glovo_gradle-versioning-plugin")
        property("sonar.projectName", "gradle-versioning-plugin")
        property("sonar.organization", "glovo")
        property("sonar.verbose", "true")
        property("sonar.java.coveragePlugin", "jacoco")
        property("sonar.coverage.jacoco.xmlReportPaths", "**/reports/jacoco/**.xml")
        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.pullrequest.provider", "github")
        property("sonar.pullrequest.github.repository", "glovo/gradle-versioning-plugin")
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "jacoco")

    group = "com.glovoapp.gradle"

    dependencies {
        fun setupTestDependencies(configuration: String) {
            val junitVersion = "5.7.0"

            configuration(gradleTestKit())
            configuration("org.junit.jupiter:junit-jupiter-params:$junitVersion")
            configuration("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
        }

        setupTestDependencies("testImplementation")

        plugins.withType<JavaTestFixturesPlugin> {
            setupTestDependencies("testFixturesApi")
        }
    }

    configure<JavaPluginExtension> {
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    tasks.withType<Test> {
        systemProperty("tests.tmp.dir", temporaryDir)

        useJUnitPlatform()
        doLast { sleep(1000) /* sometimes it throws Unable to read execution data file */ }
    }

    tasks.withType<JacocoReport> {
        reports.xml.required.set(true)
        reports.html.required.set(true)
        dependsOn(tasks.withType<Test>())
    }

    rootProject.tasks.named(SONARQUBE_TASK_NAME).configure {
        dependsOn(tasks.withType<JacocoReport>())
    }

    plugins.withType<PublishingPlugin> {
        configure<PublishingExtension> {
            publications.withType<MavenPublication> {
                artifactId = the<BasePluginExtension>().archivesName.apply { disallowChanges() }.get()
            }
            repositories { maven(file("$rootDir/.m2local")) { name = "Local" } }
        }
    }

    plugins.withType<JavaGradlePluginPlugin> {
        val outputDir = layout.buildDirectory.dir("generated/plugins/dsl")
        val extension = the<GradlePluginDevelopmentExtension>()

        val generateDSL by lazy {
            tasks.register("generatePluginsDSL") {
                outputs.dir(outputDir)

                onlyIf { extension.plugins.isNotEmpty() }
                doLast {
                    outputDir.get().asFile.apply { deleteRecursively(); mkdirs() }
                    outputDir.get().file("VersioningPluginDSL.kt").asFile.writeText(
                        extension.plugins
                            .filter { it.displayName != null }
                            .joinToString(separator = "\n\n", prefix = "@file:JvmMultifileClass\n\n") {
                                """
                                    val org.gradle.plugin.use.PluginDependenciesSpec.`${it.displayName}`
                                        get() = id("${it.id}")
                                """.trimIndent()
                            }
                    )
                }
            }
        }

        afterEvaluate {
            extension.plugins.all {
                generateDSL.configure {
                    inputs.property("plugin:$id", implementationClass)
                }
            }
        }

        plugins.withType<KotlinPluginWrapper> {
            project.the<KotlinSingleTargetExtension>()
                .sourceSets["main"]
                .kotlin
                .srcDir(files(outputDir).builtBy(generateDSL))
        }
    }

}
