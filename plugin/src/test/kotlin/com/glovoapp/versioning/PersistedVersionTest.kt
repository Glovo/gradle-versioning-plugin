package com.glovoapp.versioning

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class PersistedVersionTest {

    @get:Rule
    val temp = TemporaryFolder()

    private val propertiesFile by lazy {
        File(temp.root, "test.properties").apply {
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
