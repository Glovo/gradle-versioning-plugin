plugins {
    `kotlin-library`
    `code-coverage`
    `maven-central-publish`
}

description = "Semantic Versioning Library"
base.archivesName.set("versioning")

dependencies {
    junit5()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifactId = tasks.jar.get().archiveBaseName.get()
        }
    }
}
