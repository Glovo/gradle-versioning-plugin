plugins {
    `kotlin-dsl`
}

base.archivesBaseName = "gradle-android-versioning-plugin"

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
    api("com.android.tools.build:gradle:4.1.1")

    testImplementation(testFixtures(project(":plugin")))
}
