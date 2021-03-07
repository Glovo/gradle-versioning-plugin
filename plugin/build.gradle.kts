plugins {
    `kotlin-dsl`
    `java-test-fixtures`
}

base.archivesBaseName = "gradle-versioning-plugin"

gradlePlugin {
    plugins {
        create("semantic-versioning") {
            id = "com.glovoapp.semantic-versioning"
            implementationClass = "com.glovoapp.versioning.SemanticVersioningPlugin"
        }
    }
}

dependencies {
    api(project(":library"))
}
