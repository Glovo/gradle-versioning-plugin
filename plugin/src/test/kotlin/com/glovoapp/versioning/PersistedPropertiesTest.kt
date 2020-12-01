package com.glovoapp.versioning

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.io.FileNotFoundException

class PersistedPropertiesTest {

    @get:Rule
    val temp = TemporaryFolder()

    private val propertiesFile by lazy {
        File(temp.root, "test.properties").apply {
            writeText("one=1")
        }
    }

    private val properties by lazy { PersistedProperties(propertiesFile) }

    @Test(expected = FileNotFoundException::class)
    fun throwsWhenFileNotFoundAsSoonPropertiesAccessed() {
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
