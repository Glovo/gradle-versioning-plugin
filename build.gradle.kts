import com.github.gmazzo.gradle.plugins.BuildConfigExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.sonarqube.gradle.SonarQubeExtension.SONARQUBE_TASK_NAME
import java.lang.Thread.sleep

plugins {
    kotlin("jvm") version embeddedKotlinVersion apply false
    id("com.github.gmazzo.buildconfig") version "3.0.2" apply false
    id("com.glovoapp.artifactory") version "0.1.16"
    id("com.glovoapp.semantic-versioning") version "0.1.19"
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

    configure<JavaPluginExtension>() {
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
            repositories {
                maven("${rootProject.buildDir}/repo") { name = "local" }
            }
            publications.withType<MavenPublication> {
                artifactId = the<BasePluginConvention>().archivesBaseName
            }
        }
    }

    plugins.withType<JavaGradlePluginPlugin> {
        apply(plugin = "com.github.gmazzo.buildconfig")

        configure<BuildConfigExtension> {
            useKotlinOutput {
                topLevelConstants = true
                internalVisibility = true
            }

            configure<GradlePluginDevelopmentExtension> {
                plugins.all {
                    buildConfigField("String", "PLUGIN_ID", provider { "\"${this@all.id}\"" })
                    buildConfigField("String", "PLUGIN_VERSION", provider { "\"${project.version}\"" })
                    buildConfigField("String", "PLUGIN_ARTIFACT", provider {
                        "\"\${PLUGIN_ID}:\${PLUGIN_ID}.gradle.plugin:\${PLUGIN_VERSION}\""
                    })
                }
            }
        }
    }

}
