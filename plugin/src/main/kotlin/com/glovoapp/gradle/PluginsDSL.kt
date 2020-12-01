package com.glovoapp.gradle

import org.gradle.plugin.use.PluginDependenciesSpec

val PluginDependenciesSpec.`semantic-versioning`
    get() = id("com.glovoapp.semantic-versioning")

val PluginDependenciesSpec.`android-versioning`
    get() = id("com.glovoapp.android-versioning")
