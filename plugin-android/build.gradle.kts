plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish")
    id("pl.droidsonroids.jacoco.testkit")
}

base.archivesName.set("gradle-android-versioning-plugin")

gradlePlugin {
    plugins {
        create("android-versioning") {
            id = "com.glovoapp.android-versioning"
            displayName = "android-versioning"
            implementationClass = "com.glovoapp.versioning.AndroidVersioningPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/Glovo/gradle-versioning-plugin"
    vcsUrl = "https://github.com/Glovo/gradle-versioning-plugin"
    description = "An Android Gradle plugin implementing https://semver.org/"
    tags = listOf("gradle", "versioning", "semantic-versioning", "java", "kotlin", "android")
}

dependencies {
    api(project(":plugin"))

    implementation("com.android.tools.build:gradle:7.0.0")

    testImplementation(testFixtures(project(":plugin")))
}
