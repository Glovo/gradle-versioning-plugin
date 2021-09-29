package com.glovoapp.versioning

import java.util.*

fun PersistedProperties.semanticVersion(key: String) =
    cachedVersion(key, SemanticVersion.Companion::parse)

fun PersistedProperties.numericVersion(key: String) =
    cachedVersion(key, String::toInt)

class PersistedVersion<Type : Any>(
    private val properties: PersistedProperties,
    private val key: String,
    private val parser: (String) -> Type,
    private val toRaw: Type.() -> String = { toString() }
) {

    private val onChangeListeners: MutableList<(Type) -> Unit> = LinkedList()

    var value: Type
        get() = checkNotNull(properties.getProperty(key)) { "Missing property: $key in ${properties.file}" }.let(parser)
        set(value) {
            properties.setProperty(key, value.toRaw())
        }

    fun onChanged(action: (Type) -> Unit) {
        onChangeListeners.add(action)
        action(value)
    }

    override fun toString() = value.toString()

}
