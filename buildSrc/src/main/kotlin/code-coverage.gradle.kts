import org.sonarqube.gradle.SonarQubeExtension.SONARQUBE_TASK_NAME

plugins {
    jacoco
    id("org.sonarqube")
}

sonarqube {
    properties {
        property("sonar.projectKey", "Glovo_gradle-versioning-plugin")
        property("sonar.projectName", "gradle-versioning-plugin")
        property("sonar.organization", "glovo")
        property("sonar.verbose", "true")
        property("sonar.java.coveragePlugin", "jacoco")
        property("sonar.coverage.jacoco.xmlReportPaths", "**/reports/jacoco/**.xml")
        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.pullrequest.provider", "github")
        property("sonar.pullrequest.github.repository", "glovo/gradle-versioning-plugin")
    }
}

tasks.named(SONARQUBE_TASK_NAME).configure {
    dependsOn(tasks.withType<JacocoReport>())
}

tasks.withType<Test> {
    systemProperty("tests.tmp.dir", temporaryDir)

    useJUnitPlatform()
    doLast { Thread.sleep(1000) } // sometimes it throws Unable to read execution data file
}

tasks.withType<JacocoReport> {
    reports.xml.required.set(true)
    reports.html.required.set(true)
    dependsOn(tasks.withType<Test>())
}
