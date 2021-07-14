import com.github.gmazzo.gradle.plugins.BuildConfigExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version embeddedKotlinVersion apply false
    id("com.github.gmazzo.buildconfig") version "3.0.2" apply false
    id("com.glovoapp.artifactory") version "0.1.16"
    id("com.glovoapp.semantic-versioning") version "0.1.16"
}

subprojects {
    apply(plugin = "java")

    group = "com.glovoapp.gradle"

    dependencies {
        fun setupTestDependencies(configuration: String) {
            configuration(gradleTestKit())
            configuration("org.junit.jupiter:junit-jupiter-params:5.7.0")
            configuration("org.junit.jupiter:junit-jupiter-engine:5.7.0")
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

            afterEvaluate {
                configure<GradlePluginDevelopmentExtension> {
                    plugins.all {
                        buildConfigField("String", "PLUGIN_ID", "\"${this@all.id}\"")
                        buildConfigField("String", "PLUGIN_VERSION", provider { "\"${project.version}\"" })
                        buildConfigField("String", "PLUGIN_ARTIFACT", provider {
                            "\"\${PLUGIN_ID}:\${PLUGIN_ID}.gradle.plugin:\${PLUGIN_VERSION}\""
                        })
                    }
                }
            }
        }
    }

}
