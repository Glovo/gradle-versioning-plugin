plugins {
    `kotlin-dsl`
    `java-test-fixtures`
    `gradle-plugin`
    `code-coverage`
    id("pl.droidsonroids.jacoco.testkit")
    id("com.github.gmazzo.buildconfig") version "3.0.3"
}

description = "Semantic Versioning Gradle Plugin"
base.archivesName.set("gradle-versioning-plugin")

dependencies {
    api(project(":library"))
    testFixturesApi(gradleTestKit())
    junit5("testFixturesApi")
}

buildConfig {
    useKotlinOutput { internalVisibility = true }

    buildConfigField("String", "PLUGIN_VERSION", provider { "\"${project.version}\"" })
}

gradlePlugin {
    plugins {
        create("semantic-versioning") {
            id = "com.glovoapp.semantic-versioning"
            displayName = "semantic-versioning"
            implementationClass = "com.glovoapp.versioning.SemanticVersioningPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/Glovo/gradle-versioning-plugin"
    vcsUrl = "https://github.com/Glovo/gradle-versioning-plugin"
    description = "A Gradle plugin implementing https://semver.org/"
    tags = listOf("gradle", "versioning", "semantic-versioning", "java", "kotlin")
}

tasks.named("publish") {
    mustRunAfter(":library:publish")
}
