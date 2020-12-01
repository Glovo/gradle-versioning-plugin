package com.glovoapp.versioning

import java.io.File
import java.util.Properties

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
