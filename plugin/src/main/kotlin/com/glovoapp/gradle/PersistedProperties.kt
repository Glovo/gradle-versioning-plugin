package com.glovoapp.gradle

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import java.io.File
import java.util.Properties
import java.io.FileInputStream
import java.io.FileOutputStream

class PersistedProperties(
        private val file: File
) : Properties() {

    init {
        file.reader().use(::load)
    }

    override fun put(key: Any?, value: Any?) =
            super.put(key, value).also { flush() }

    override fun putAll(from: Map<*, *>) =
            super.putAll(from).also { flush() }

    override fun remove(key: Any?) =
            super.remove(key).also { flush() }

    override fun clear() =
            super.clear().also { flush() }

    fun flush()  {
        file.writer().use { store(it, null) }
    }

}
