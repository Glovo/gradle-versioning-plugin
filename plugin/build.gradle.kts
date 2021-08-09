plugins {
    `kotlin-dsl`
    `java-test-fixtures`
    id("pl.droidsonroids.jacoco.testkit")
    id("com.gradle.plugin-publish") version "0.15.0"
}

base.archivesName.set("gradle-versioning-plugin")

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
    val repoUrl = "https://github.com/Glovo/gradle-versioning-plugin"
    website = repoUrl
    vcsUrl = repoUrl
    description = "A Gradle plugin implementing https://semver.org/"
    tags = listOf("gradle", "semantic-versioning", "java", "kotlin", "android")
}

dependencies {
    api(project(":library"))
}
