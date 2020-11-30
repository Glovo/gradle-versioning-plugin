repositories {
    google()
    mavenCentral()
    jcenter()
}

plugins {
    groovy
}

dependencies {
    implementation(gradleApi())
    compileOnly("com.android.tools.build:gradle:4.0.1")

    testImplementation("junit:junit:4.12")
    testImplementation("com.google.truth:truth:0.45")
}
