package com.glovoapp.versioning

import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * A [Properties] that dumps its content to the given [file] on each change.
 *
 * Only the very basic [MutableMap] methods are dirty safe (linked with the backed file)
 *
 * @constructor creates a new [PersistedProperties]. No exception will be thrown if [file] does not exists.
 */
class PersistedProperties(
    val file: File
) : Properties() {

    private val cache = ConcurrentHashMap<String, PersistedVersion<*>>()
    private var loading = false
    private var lastSync = 0L

    init {
        reload()
    }

    override fun get(key: Any?) = reloadIfDirty().let { super.get(key) }

    override fun getProperty(key: String): String? = reloadIfDirty().let { super.getProperty(key) }

    override fun put(key: Any?, value: Any?) = super.put(key, value)
        .also { if (it != value) { flush() } }

    override fun putAll(from: Map<*, *>) {
        val changed = from.entries.fold(false) { acc, (key, value) ->
            acc or (super.put(key, value) != value)
        }

        if (changed) { flush() }
    }

    override fun remove(key: Any?) = super.remove(key)
        .also { if (it != null) { flush() } }

    override fun clear() = super.clear().also { flush() }

    private fun reloadIfDirty() {
        if (isDirty) {
            reload()
        }
    }

    val isDirty get() = file.lastModified() != lastSync

    fun flush() {
        if (!loading) {
            file.writer().use { store(it, null) }
            lastSync = file.lastModified()
        }
    }

    fun reload() {
        loading = true
        try {
            file.takeIf(File::isFile)?.reader()?.use(::load)

        } finally {
            loading = false
        }
        lastSync = file.lastModified()
    }

    @Suppress("UNCHECKED_CAST")
    internal inline fun <reified Type : Any> cachedVersion(key: String, noinline parser: (String) -> Type) = cache
        .compute(key) { _, current ->
            current
                ?.takeUnless { it.value !is Type }
                ?: PersistedVersion(this, key = key, parser = parser)
        } as PersistedVersion<Type>

}
