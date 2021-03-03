package com.glovoapp.versioning

import org.gradle.plugin.use.PluginDependenciesSpec

val PluginDependenciesSpec.`semantic-versioning`
    get() = id("com.glovoapp.semantic-versioning")
