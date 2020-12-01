package com.glovoapp.versioning

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.io.FileNotFoundException

class PersistedPropertiesTest {

    @TempDir
    lateinit var tmpDir: File

    private val propertiesFile by lazy {
        File(tmpDir, "test.properties").apply {
            writeText("one=1")
        }
    }

    private val properties by lazy { PersistedProperties(propertiesFile) }

    @Test
    fun throwsWhenFileNotFoundAsSoonPropertiesAccessed() = assertThrows<FileNotFoundException> {
        propertiesFile.delete()

        properties["foo"]
        fail("FileNotFoundException expected but not thrown")
    }

    @Test
    fun returnsValueForSpecifiedKey() {
        assertEquals("1", properties["one"])
    }

    @Test
    fun updatesValueForSpecifiedKey() {
        properties["one"] = "uno"

        assertEquals("uno", properties["one"])
    }

    @Test
    fun persistsUpdatedValueForSpecifiedKey() {
        properties["one"] = "uno"

        assertTrue("one=uno" in propertiesFile.readLines())
    }

    @Test
    fun returnsTrueWhenContainsValueForSpecifiedKey() {
        assertTrue("one" in properties.keys)
    }

    @Test
    fun returnsFalseWhenContainsNoValueForSpecifiedKey() {
        assertFalse("foo" in properties.keys)
    }

}
