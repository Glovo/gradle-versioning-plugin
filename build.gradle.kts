plugins {
    id("com.glovoapp.artifactory") version "0.1.9"
}

allprojects {
    group = "com.glovoapp.gradle"
    version = "0.0.2"

    repositories {
        google()
        mavenCentral()
    }

    plugins.withType<PublishingPlugin> {
        configure<PublishingExtension>  {
            repositories {
                maven("${rootProject.buildDir}/repo") { name = "local" }
            }
        }
    }

}
