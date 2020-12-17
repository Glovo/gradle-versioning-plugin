plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        create("semantic-versioning") {
            id = "com.glovoapp.semantic-versioning"
            implementationClass = "com.glovoapp.versioning.SemanticVersioningPlugin"
        }
        create("android-versioning") {
            id = "com.glovoapp.android-versioning"
            implementationClass = "com.glovoapp.versioning.AndroidVersioningPlugin"
        }
    }
}

dependencies {
    implementation("com.android.tools.build:gradle:4.1.1")

    testImplementation(gradleTestKit())
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

tasks.withType<Test> {
    systemProperty("tests.tmp.dir", temporaryDir)

    useJUnitPlatform()
}
