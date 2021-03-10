import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version embeddedKotlinVersion apply false
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

}
