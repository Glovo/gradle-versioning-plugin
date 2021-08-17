pluginManagement {
    repositories {
        maven(file("$rootDir/.m2local")) { name = "Local" }
        gradlePluginPortal()
    }
}

rootProject.name = "gradle-versioning-plugin"

include(":library")
include(":plugin")
include(":plugin-android")
