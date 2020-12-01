plugins {
    `kotlin-dsl`
}

base.archivesBaseName = "semantic-version"

dependencies {
    compileOnly("com.android.tools.build:gradle:4.0.1")

    testImplementation("junit:junit:4.12")
    testImplementation("com.google.truth:truth:0.45")
}
