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

    private var rawValue: String? = null

    var value: Type = @Suppress("UNCHECKED_CAST") (Any() as Type)
        @Synchronized get() {
            val actualValue = checkNotNull(properties.getProperty(key)) {
                "Missing property: $key in ${properties.file}"
            }

            if (rawValue != actualValue) {
                value = parser(actualValue)
            }
            return field
        }
        @Synchronized set(value) {
            field = value
            rawValue = value.toRaw()
            properties.setProperty(key, rawValue)
            onChangeListeners.forEach { it(value) }
        }

    fun onChanged(action: (Type) -> Unit) {
        onChangeListeners.add(action)
        action(value)
    }

    override fun toString() = value.toString()

}
