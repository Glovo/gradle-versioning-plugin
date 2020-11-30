repositories {
    google()
    mavenCentral()
    jcenter()
}

plugins {
    groovy
}

dependencies {
    implementation (gradleApi())
    implementation (project(":plugin:core"))
    implementation (embeddedKotlin("stdlib"))

    compileOnly("com.android.tools.build:gradle:4.0.1")
}
