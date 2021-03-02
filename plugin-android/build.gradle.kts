plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        create("android-versioning") {
            id = "com.glovoapp.android-versioning"
            implementationClass = "com.glovoapp.versioning.AndroidVersioningPlugin"
        }
    }
}

dependencies {
    implementation(project(":versioning"))
    implementation("com.android.tools.build:gradle:4.1.1")

    testImplementation(testFixtures(project(":versioning")))
}
