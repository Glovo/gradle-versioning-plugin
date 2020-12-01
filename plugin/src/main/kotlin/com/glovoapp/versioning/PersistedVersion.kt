package com.glovoapp.versioning

import java.util.*

fun PersistedProperties.semanticVersion(key: String) =
        PersistedVersion(this, key = key, parser = SemanticVersion::parse)

fun PersistedProperties.numericVersion(key: String) =
        PersistedVersion(this, key = key, parser = String::toInt)

class PersistedVersion<Type : Any>(
        private val properties: PersistedProperties,
        private val key: String,
        private val parser: (String) -> Type,
        private val toRaw: Type.() -> String = { toString() }
) {

    private val onChangeListeners : MutableList<(Type) -> Unit> = LinkedList()

    var value: Type
        get() = properties.getProperty(key).let(parser)
        set(value) {
            properties.setProperty(key, value.toRaw())
        }

    fun onChanged(action: (Type) -> Unit) {
        onChangeListeners.add(action)
        action(value)
    }

    override fun toString() = value.toString()

}
