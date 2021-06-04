package com.glovoapp.versioning

import org.gradle.api.Plugin
import org.gradle.api.Project

class PersistedVersionPlugin : Plugin<Project> {

    lateinit var persistedProperties: PersistedProperties

    override fun apply(target: Project) = with(target) {
        check(this == rootProject) { "${this::class.java} should be applied from root project" }

        persistedProperties = PersistedProperties(file("version.properties"))
    }

}
