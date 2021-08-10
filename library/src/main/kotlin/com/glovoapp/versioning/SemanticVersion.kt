package com.glovoapp.versioning;

data class SemanticVersion(
    val major: Int,
    val minor: Int? = null,
    val patch: Int? = null,
    val preRelease: String? = null,
    val build: String? = null
) : Comparable<SemanticVersion> {

    enum class Increment {

        MAJOR, MINOR, PATCH;

        operator fun times(amount: Int) = this to amount

    }

    companion object {

        // https://semver.org/#is-there-a-suggested-regular-expression-regex-to-check-a-semver-string
        private val VERSION_PATTERN =
            "(0|[1-9]\\d*)(?:\\.(0|[1-9]\\d*)(?:\\.(0|[1-9]\\d*))?)?(?:-((?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?".toRegex(
                RegexOption.IGNORE_CASE
            )

        fun parse(source: String) = with(VERSION_PATTERN.matchEntire(source)) {
            if (this == null) {
                throw  IllegalArgumentException("Unsupported format for semantic version: $source")
            }

            SemanticVersion(
                major = groupValues[1].toInt(),
                minor = groupValues[2].toIntOrNull(),
                patch = groupValues[3].toIntOrNull(),
                preRelease = groupValues[4].takeIf { it.isNotBlank() },
                build = groupValues[5].takeIf { it.isNotBlank() }
            )
        }

        fun String.toVersion() = parse(this)

    }

    val preReleaseIdentifiers by lazy { preRelease?.split('.') ?: emptyList() }

    val buildIdentifiers by lazy { build?.split('.') ?: emptyList() }

    operator fun plus(increment: Increment) = plus(increment * 1)

    operator fun plus(increment: Pair<Increment, Int>) = when (increment.first) {
        Increment.MAJOR -> copy(major = major + increment.second, minor = 0, patch = 0)
        Increment.MINOR -> copy(minor = (minor ?: 0) + increment.second, patch = 0)
        Increment.PATCH -> copy(patch = (patch ?: 0) + increment.second)
    }

    // https://semver.org/#spec-item-11
    override fun compareTo(other: SemanticVersion) = when {
        major != other.major -> major.compareTo(other.major)
        minor != other.minor -> minor.compareTo(other.minor)
        patch != other.patch -> patch.compareTo(other.patch)
        preRelease == null -> if (other.preRelease == null) 0 else 1
        other.preRelease == null -> -1
        else -> preReleaseIdentifiers.compareTo(other.preReleaseIdentifiers)
    }

    private fun Int?.compareTo(other: Int?) = when(this) {
        null -> if (other == null) 0 else -1
        else -> if (other == null) 1 else compareTo(other)
    }

    private tailrec fun List<String>.compareTo(other: List<String>, index: Int = 0): Int = when {
        index >= size -> if (index >= other.size) 0 else -1
        index >= other.size -> 1
        else -> {
            val thisValue = this[index]
            val otherValue = other[index]
            val thisNumber = thisValue.toIntOrNull()
            val otherNumber = otherValue.toIntOrNull()

            val compare = when {
                thisNumber == null -> if (otherNumber != null) 1 else thisValue.compareTo(otherValue)
                otherNumber == null -> -1
                else -> thisNumber.compareTo(otherNumber)
            }

            if (compare != 0) compare else compareTo(other, index + 1)
        }
    }

    private fun String?.prefix(prefix: String) =
        if (isNullOrBlank()) "" else prefix + this

    override fun toString() = "$major${minor.versionStr}${patch.versionStr}${preRelease.prefix("-")}${build.prefix("+")}"

    private inline val Int?.versionStr
        get() = when (this) {
            null -> ""
            else -> ".$this"
        }

}
