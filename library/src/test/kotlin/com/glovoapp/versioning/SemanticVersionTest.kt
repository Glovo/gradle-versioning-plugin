package com.glovoapp.versioning

import com.glovoapp.versioning.SemanticVersion.Companion.toVersion
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.of
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.random.Random

class SemanticVersionTest {

    private val random = Random(0) // for deterministic shuffle

    @Test
    fun `parse, throws an error if can't parse`() {
        assertThrows<IllegalArgumentException> {
            SemanticVersion.parse("x.y.z")
        }
    }

    @ParameterizedTest
    @MethodSource("versions")
    fun `parse, returns a version`(source: String, expected: SemanticVersion) {
        val version = source.toVersion()

        assertEquals(expected, version)
    }

    @ParameterizedTest
    @MethodSource("versions")
    fun `toString, returns raw version string`(expected: String, version: SemanticVersion) {
        val actual = version.toString()

        assertEquals(expected, actual)
    }

    @ParameterizedTest
    @MethodSource("versions")
    @Suppress("UNUSED_PARAMETER")
    fun `preReleaseIdentifiers, parses the preRelease value`(unused: String, version: SemanticVersion) {
        val expected = version.preRelease?.split('.') ?: emptyList()

        assertEquals(expected, version.preReleaseIdentifiers)
    }

    @ParameterizedTest
    @MethodSource("versions")
    @Suppress("UNUSED_PARAMETER")
    fun `buildIdentifiers, parses the build value`(unused: String, version: SemanticVersion) {
        val expected = version.build?.split('.') ?: emptyList()

        assertEquals(expected, version.buildIdentifiers)
    }

    @ParameterizedTest
    @MethodSource("increments")
    fun `plus, returns the incremented version`(version: SemanticVersion, increment: SemanticVersion.Increment) {
        val expected = when (increment) {
            SemanticVersion.Increment.MAJOR -> version.copy(major = version.major + 1, minor = 0, patch = 0)
            SemanticVersion.Increment.MINOR -> version.copy(minor = (version.minor ?: 0) + 1, patch = 0)
            SemanticVersion.Increment.PATCH -> version.copy(patch = (version.patch ?: 0) + 1)
        }
        val actual = version + increment

        assertEquals(expected, actual)
    }

    @ParameterizedTest
    @MethodSource("versionsForCompare")
    fun `compare, returns if older or newer than other`(expected: List<SemanticVersion>) {
        // in case of any error, the list will be not in the original order
        val actual = expected.shuffled(random).sorted()

        assertEquals(expected, actual)
    }

    @ParameterizedTest
    @MethodSource("simplifiedVersions")
    fun `compare, simplified versions are equals but it's string representation is different`(
        simplifiedVersion: String,
        expectedFullVersion: String
    ) {
        val simplified = simplifiedVersion.toVersion()
        val expected = expectedFullVersion.toVersion()

        assertNotEquals(simplified, expected)
        assertEquals(simplified.toString(), simplifiedVersion)
        assertNotEquals(simplified.toString(), expected.toString())
    }

    companion object {

        @JvmStatic
        fun versions() = Stream.of(
            of("0.0.4", SemanticVersion(0, 0, 4)),
            of("1", SemanticVersion(1)),
            of("1.2", SemanticVersion(1, 2)),
            of("1.2.3", SemanticVersion(1, 2, 3)),
            of("10.20.30", SemanticVersion(10, 20, 30)),
            of("1.1.2-prerelease+meta", SemanticVersion(1, 1, 2, "prerelease", "meta")),
            of("1.1.2+meta", SemanticVersion(1, 1, 2, null, "meta")),
            of("1.1.2+meta-valid", SemanticVersion(1, 1, 2, null, "meta-valid")),
            of("1.0.0-alpha", SemanticVersion(1, 0, 0, "alpha")),
            of("1.0.0-beta", SemanticVersion(1, 0, 0, "beta")),
            of("1.0.0-alpha.beta", SemanticVersion(1, 0, 0, "alpha.beta")),
            of("1.0.0-alpha.beta.1", SemanticVersion(1, 0, 0, "alpha.beta.1")),
            of("1.0.0-alpha.1", SemanticVersion(1, 0, 0, "alpha.1")),
            of("1.0.0-alpha0.valid", SemanticVersion(1, 0, 0, "alpha0.valid")),
            of("1.0.0-alpha.0valid", SemanticVersion(1, 0, 0, "alpha.0valid")),
            of(
                "1.0.0-alpha-a.b-c-somethinglong+build.1-aef.1-its-okay",
                SemanticVersion(1, 0, 0, "alpha-a.b-c-somethinglong", "build.1-aef.1-its-okay")
            ),
            of("1.0.0-rc.1+build.1", SemanticVersion(1, 0, 0, "rc.1", "build.1")),
            of("2.0.0-rc.1+build.123", SemanticVersion(2, 0, 0, "rc.1", "build.123")),
            of("1.2.3-beta", SemanticVersion(1, 2, 3, "beta")),
            of("10.2.3-DEV-SNAPSHOT", SemanticVersion(10, 2, 3, "DEV-SNAPSHOT")),
            of("1.2.3-SNAPSHOT-123", SemanticVersion(1, 2, 3, "SNAPSHOT-123")),
            of("1.0.0", SemanticVersion(1, 0, 0)),
            of("2.0.0", SemanticVersion(2, 0, 0)),
            of("1.1.7", SemanticVersion(1, 1, 7)),
            of("2.0.0+build.1848", SemanticVersion(2, 0, 0, null, "build.1848")),
            of("2.0.1-alpha.1227", SemanticVersion(2, 0, 1, "alpha.1227")),
            of("1.0.0-alpha+beta", SemanticVersion(1, 0, 0, "alpha", "beta")),
            of(
                "1.2.3----RC-SNAPSHOT.12.9.1--.12+788",
                SemanticVersion(1, 2, 3, "---RC-SNAPSHOT.12.9.1--.12", "788")
            ),
            of("1.2.3----R-S.12.9.1--.12+meta", SemanticVersion(1, 2, 3, "---R-S.12.9.1--.12", "meta")),
            of("1.2.3----RC-SNAPSHOT.12.9.1--.12", SemanticVersion(1, 2, 3, "---RC-SNAPSHOT.12.9.1--.12")),
            of(
                "1.0.0+0.build.1-rc.10000aaa-kk-0.1",
                SemanticVersion(1, 0, 0, null, "0.build.1-rc.10000aaa-kk-0.1")
            ),
            of("1.0.0-0A.is.legal", SemanticVersion(1, 0, 0, "0A.is.legal"))
        )

        @JvmStatic
        fun increments() = versions().map { it.get()[1] }.flatMap { version ->
            SemanticVersion.Increment.values().map { increment ->
                of(version, increment)
            }.stream()
        }

        @JvmStatic
        fun versionsForCompare() = Stream.of(
            of(
                listOf(
                    SemanticVersion(1, 0, 0),
                    SemanticVersion(2, 0, 0),
                    SemanticVersion(2, 1, 0),
                    SemanticVersion(2, 1, 1)
                )
            ),
            of(
                listOf(
                    SemanticVersion(1, 0, 0, "alpha"),
                    SemanticVersion(1, 0, 0)
                )
            ),
            of(
                listOf(
                    SemanticVersion(1, 0, 0, "alpha"),
                    SemanticVersion(1, 0, 0, "alpha.1"),
                    SemanticVersion(1, 0, 0, "alpha.beta"),
                    SemanticVersion(1, 0, 0, "beta"),
                    SemanticVersion(1, 0, 0, "beta.2"),
                    SemanticVersion(1, 0, 0, "beta.11"),
                    SemanticVersion(1, 0, 0, "rc.1"),
                    SemanticVersion(1, 0, 0)
                )
            ),
            of(
                listOf(
                    SemanticVersion(1),
                    SemanticVersion(1, 0),
                    SemanticVersion(1, 0, 0),
                    SemanticVersion(2),
                    SemanticVersion(2, 0),
                    SemanticVersion(2, 1),
                    SemanticVersion(2, 1, 0),
                    SemanticVersion(2, 1, 1)
                )
            ),
        )

        @JvmStatic
        fun simplifiedVersions() = Stream.of(
            of("1", "1.0.0"),
            of("1.0", "1.0.0"),
            of("7.1", "7.1.0"),
        )

    }

}
