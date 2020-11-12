package com.glovoapp.gradle

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import java.io.File
import java.util.Properties
import java.io.FileInputStream
import java.io.FileOutputStream

class PersistedSemanticVersion(
        @get:InputFile val propertiesFile: File,
        @get:Input val key: String = "version"
) {

    private val properties by lazy { Properties().also { propertiesFile.reader().use(it::load) } }

    @get:Internal
    var value: SemanticVersion
        get() = properties.getProperty(key).let(SemanticVersion::parse)
        set(value) {
            properties[key] = value.toString()
        }

    fun flush() = propertiesFile.writer().use {
        properties.store(it, null)
    }

    override fun toString() = value.toString()

}
