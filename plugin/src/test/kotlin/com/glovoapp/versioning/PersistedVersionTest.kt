package com.glovoapp.versioning

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class PersistedVersionTest {

    @TempDir
    lateinit var tmpDir: File

    private val propertiesFile by lazy {
        File(tmpDir, "test.properties").apply {
            writeText("version=10")
        }
    }

    private val properties by lazy { PersistedProperties(propertiesFile) }

    private val version by lazy { properties.numericVersion("version") }

    @Test
    fun readsValueFromFile() {
        assertEquals(10, version.value)
    }

    @Test
    fun writesValueToFile() {
        version.value = 100

        assertTrue("version=100" in propertiesFile.readLines())
    }

    @Test
    fun incrementsValue() {
        version.value += 1

        assertTrue("version=11" in propertiesFile.readLines())
    }

}
