plugins {
    `kotlin-dsl`
    `java-test-fixtures`
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

dependencies {
    api(project(":library"))
}
