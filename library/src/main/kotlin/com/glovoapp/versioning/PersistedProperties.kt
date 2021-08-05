package com.glovoapp.versioning

import java.io.File
import java.util.*

/**
 * A [Properties] that dumps its content to the given [file] on each change.
 *
 * @constructor creates a new [PersistedProperties]. No exception will be thrown if [file] does not exists.
 */
class PersistedProperties(
    val file: File
) : Properties() {

    private var initialized = false

    init {
        file.takeIf(File::isFile)?.reader()?.use(::load)
        initialized = true
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
        if (initialized) {
            file.writer().use { store(it, null) }
        }
    }

}
