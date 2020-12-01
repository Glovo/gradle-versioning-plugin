package com.glovoapp.gradle;

data class SemanticVersion(val major: Int, val minor: Int, val patch: Int) {

    enum class Increment { MAJOR, MINOR, PATCH }

    companion object {
        private val VERSION_PATTERN = "(\\d+)\\.(\\d+)\\.(\\d+)".toRegex()

        fun parse(source: String) = with(VERSION_PATTERN.matchEntire(source)) {
            checkNotNull(this) { "Unsupported format for semantic version: $source" }

            SemanticVersion(
                    major = groupValues[1].toInt(),
                    minor = groupValues[2].toInt(),
                    patch = groupValues[3].toInt())
        }

    }

    operator fun plus(increment: Increment) = when (increment) {
        Increment.MAJOR -> copy(major = major + 1, minor = 0, patch = 0)
        Increment.MINOR -> copy(minor = minor + 1, patch = 0)
        Increment.PATCH -> copy(patch = patch + 1)
    }

    override fun toString() = "$major.$minor.$patch"

}
