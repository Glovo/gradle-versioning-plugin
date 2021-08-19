plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation(embeddedKotlin("gradle-plugin"))
    implementation("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:3.3")
    implementation("gradle.plugin.pl.droidsonroids.gradle.jacoco:jacoco-gradle-testkit-plugin:1.0.9")
    implementation("com.gradle.publish:plugin-publish-plugin:0.15.0")
}
