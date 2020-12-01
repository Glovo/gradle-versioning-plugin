package com.glovoapp.versioning

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SemanticVersionTest {

    @Test
    fun throwsWhenParsingWrongFormat() = assertThrows<IllegalArgumentException> {
        SemanticVersion.parse("x.y.z")

        fail("Exception expected but not thrown")
    }

    @Test
    fun parsesCorrectFormat() {
        val version = SemanticVersion.parse("1.2.3")

        assertEquals(1, version.major)
        assertEquals(2, version.minor)
        assertEquals(3, version.patch)
    }

    @Test
    fun printsUsingCorrectFormat() {
        val text = "1.2.3"

        val version = SemanticVersion.parse(text)

        assertEquals(text, version.toString())
    }

    @Test
    fun incrementsMajorCorrectly() {
        val version = SemanticVersion.parse("1.2.3")

        val newVersion = version + SemanticVersion.Increment.MAJOR

        assertEquals("2.0.0", newVersion.toString())
    }

    @Test
    fun incrementsMinorCorrectly() {
        val version = SemanticVersion.parse("1.2.3")

        val newVersion = version + SemanticVersion.Increment.MINOR

        assertEquals("1.3.0", newVersion.toString())
    }

    @Test
    fun incrementsPatchCorrectly() {
        val version = SemanticVersion.parse("1.2.3")

        val newVersion = version + SemanticVersion.Increment.PATCH

        assertEquals("1.2.4", newVersion.toString())
    }

}