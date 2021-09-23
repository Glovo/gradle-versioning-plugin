plugins {
    java
    signing
    `maven-publish`
    id("org.jetbrains.dokka")
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks.named<Jar>("javadocJar") {
    from(tasks.named("dokkaJavadoc"))
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project

    if (signingKey != null && signingPassword != null) {
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(the<PublishingExtension>().publications)

    } else {
        logger.warn("Artifact signing disabled due lack of signing properties `signingKey` and `signingPassword`")
    }
}

publishing {

    publications.withType<MavenPublication> {
        // configures a default `artifactId` from `archivesName`
        artifactId = the<BasePluginExtension>().archivesName.apply { disallowChanges() }.get()

        setupMandatoryPOMAttributes()
    }

}

tasks.named("publish") {
    dependsOn("publishToSonatype")
}

// makes sure stage repository is closed and release after publishing to it
tasks.named("publishToSonatype") {
    finalizedBy(":closeAndReleaseSonatypeStagingRepository")
}

fun MavenPublication.setupMandatoryPOMAttributes() {
    pom {
        val origin = Runtime.getRuntime()
            .exec("git remote get-url origin")
            .inputStream
            .bufferedReader().use { it.readText().trim() }

        name.set("${rootProject.name}-${project.name}")
        description.set(project.description)
        url.set(origin)

        licenses {
            license {
                name.set("GNU v3")
                url.set("https://www.gnu.org/licenses/gpl-3.0")
            }
        }

        developers {
            developer {
                id.set("glovo")
                name.set("Glovo Tech")
                email.set("tech.platform@glovoapp.com")
            }
        }

        scm {
            connection.set(origin)
            developerConnection.set(origin)
            url.set(origin)
        }

    }
}
