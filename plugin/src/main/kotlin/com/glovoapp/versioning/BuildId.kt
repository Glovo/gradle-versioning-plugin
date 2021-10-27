package com.glovoapp.versioning

import org.gradle.api.invocation.Gradle
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.extra
import java.util.*

internal const val PROP_BUILD_ID = "com.glovoapp.versioning.buildId"

internal typealias BuildId = UUID

internal val Gradle.rootGradle: Gradle
    get() = when (val parent = parent) {
        null -> this
        else -> parent.rootGradle
    }

internal val Gradle.buildId: BuildId
    get() = with((rootGradle as ExtensionAware).extra) {
        runCatching { get(PROP_BUILD_ID) as BuildId }.getOrNull()
            ?: BuildId.randomUUID().also {
                set(PROP_BUILD_ID, it)

                // ensures a new UUID after each build
                rootGradle.buildFinished { set(PROP_BUILD_ID, null) }
            }
    }
