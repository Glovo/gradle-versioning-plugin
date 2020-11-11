package com.glovoapp.gradle;

import java.util.regex.Pattern

data class SemanticVersion(val major: Int, val minor: Int, val patch: Int) {

    enum class Increment(val transform: (SemanticVersion) -> SemanticVersion) {
        MAJOR({ v -> SemanticVersion(v.major + 1, 0, 0)}),
        MINOR({ v -> SemanticVersion(v.major, v.minor + 1, 0) }),
        PATCH({ v -> SemanticVersion(v.major, v.minor, v.patch + 1) });
    }

    companion object {
        private final val VERSION_PATTERN = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)")

        fun parse(source: String): SemanticVersion {
            val matcher = VERSION_PATTERN.matcher(source)
            if (!matcher.matches()) {
                throw IllegalArgumentException ("Unsupported format for semantic version: $source")
            }
            val major = matcher.group(1).toInt()
            val minor = matcher.group(2).toInt()
            val patch = matcher.group(3).toInt()
            return SemanticVersion (major, minor, patch)
        }
    }

    fun increment(increment: Increment): SemanticVersion {
        return increment.transform(this)
    }

    override fun toString(): String {
        return "$major.$minor.$patch"
    }
}
