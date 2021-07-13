plugins {
    `kotlin-dsl`
    id("pl.droidsonroids.jacoco.testkit")
}

base.archivesName.set("gradle-android-versioning-plugin")

gradlePlugin {
    plugins {
        create("android-versioning") {
            id = "com.glovoapp.android-versioning"
            implementationClass = "com.glovoapp.versioning.AndroidVersioningPlugin"
        }
    }
}

dependencies {
    api(project(":plugin"))

    implementation("com.android.tools.build:gradle:4.1.1")

    testImplementation(testFixtures(project(":plugin")))
}
