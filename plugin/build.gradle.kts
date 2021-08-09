plugins {
    `kotlin-dsl`
    `java-test-fixtures`
    id("com.gradle.plugin-publish")
    id("pl.droidsonroids.jacoco.testkit")
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
    website = "https://github.com/Glovo/gradle-versioning-plugin"
    vcsUrl = "https://github.com/Glovo/gradle-versioning-plugin"
    description = "A Gradle plugin implementing https://semver.org/"
    tags = listOf("gradle", "versioning", "semantic-versioning", "java", "kotlin")
}

dependencies {
    api(project(":library"))
}
