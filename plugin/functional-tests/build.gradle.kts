repositories {
    google()
    mavenCentral()
    jcenter()
}

plugins {
    groovy
}

dependencies {
    testImplementation(gradleTestKit())
    testImplementation("junit:junit:4.12")
    testImplementation("com.google.truth:truth:0.45")
}
