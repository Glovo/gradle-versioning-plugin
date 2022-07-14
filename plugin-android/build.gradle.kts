plugins {
    `kotlin-dsl`
    `gradle-plugin`
    `code-coverage`
    id("pl.droidsonroids.jacoco.testkit")
}

description = "Semantic Versioning Android Gradle Plugin"
base.archivesName.set("gradle-android-versioning-plugin")

dependencies {
    api(project(":plugin"))
    compileOnly("com.android.tools.build:gradle:7.1.2")
    testImplementation(testFixtures(project(":plugin")))
}

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

tasks.named("publish") {
    mustRunAfter(":plugin:publish")
}
