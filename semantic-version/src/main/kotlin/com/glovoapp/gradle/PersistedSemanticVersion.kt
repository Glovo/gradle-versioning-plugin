package com.glovoapp.gradle

import java.util.Properties
import java.io.FileInputStream
import java.io.FileOutputStream

class PersistedSemanticVersion(private val propertiesPath: String, private val key: String = "version") {

    private val properties = Properties()

    init {
        properties.load(FileInputStream(propertiesPath))
    }
    
    var value: String
        get() {
            return properties[key]!! as String
        }
        set(value) {
            properties.put(key, value)
            val stream = FileOutputStream(propertiesPath)
            properties.store(stream, "")
            stream.close()
        }

    fun apply(increment: SemanticVersion.Increment) {
        val newSemanticVersion = semanticVersion.increment(increment)
        value = newSemanticVersion.toString()
    }

    private val semanticVersion: SemanticVersion
        get() = SemanticVersion.parse(value)

    override fun toString(): String {
        return value
    }
}
