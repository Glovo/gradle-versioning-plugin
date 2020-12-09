plugins {
    id("com.glovoapp.artifactory") version "0.1.9"
    id("com.glovoapp.semantic-versioning") version "0.1.0"
}

allprojects {
    group = "com.glovoapp.gradle"

    repositories {
        google()
        jcenter()
    }

    plugins.withType<PublishingPlugin> {
        configure<PublishingExtension> {
            repositories {
                maven("${rootProject.buildDir}/repo") { name = "local" }
            }
        }
    }

}

task<Delete>("clean") {
    delete(buildDir)
}