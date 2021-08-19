plugins {
    `kotlin-dsl`
    `java-test-fixtures`
    `gradle-plugin`
    `code-coverage`
    id("pl.droidsonroids.jacoco.testkit")
}

description = "Semantic Versioning Gradle Plugin"
base.archivesName.set("gradle-versioning-plugin")

dependencies {
    api(project(":library"))
    testFixturesApi(gradleTestKit())
    junit5("testFixturesApi")
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
