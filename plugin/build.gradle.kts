plugins {
    `kotlin-dsl`
    `java-test-fixtures`
}

gradlePlugin {
    plugins {
        create("semantic-versioning") {
            id = "com.glovoapp.semantic-versioning"
            implementationClass = "com.glovoapp.versioning.SemanticVersioningPlugin"
        }
    }
}

dependencies {
    testFixturesApi(gradleTestKit())
    testFixturesApi("org.junit.jupiter:junit-jupiter-params:5.7.0")
    testFixturesApi("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

tasks.withType<Test> {
    systemProperty("tests.tmp.dir", temporaryDir)

    useJUnitPlatform()
}
