package com.glovoapp.gradle;

import java.io.Serializable
import java.util.regex.Pattern

data class SemanticVersion(val major: Int, val minor: Int, val patch: Int) {

    enum class Increment { MAJOR, MINOR, PATCH }

    companion object {
        private val VERSION_PATTERN = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)")

        fun parse(source: String) = with(VERSION_PATTERN.matcher(source)) {
            check(matches()) { "Unsupported format for semantic version: $source" }

            SemanticVersion(
                    major = group(1).toInt(),
                    minor = group(2).toInt(),
                    patch = group(3).toInt())
        }

    }

    operator fun plus(increment: Increment) = when (increment) {
        Increment.MAJOR -> copy(major = major + 1, minor = 0, patch = 0)
        Increment.MINOR -> copy(minor = minor + 1, patch = 0)
        Increment.PATCH -> copy(patch = patch + 1)
    }

    override fun toString() = "$major.$minor.$patch"

}
