plugins {
    id("maven-central-publish")
}

// makes sure to publish to mavenCentral first, before doing it to Plugins Portal
tasks.named("publish") {
    dependsOn("publishPlugins")
    mustRunAfter(":closeAndReleaseSonatypeStagingRepository")
}
