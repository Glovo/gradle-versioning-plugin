package com.glovo.mobile.release

import com.glovo.mobile.release.internal.PropertiesFile
import com.glovo.mobile.release.internal.SemanticVersion

class PersistedSemanticVersion {

    private final PropertiesFile properties
    private final String key

    PersistedSemanticVersion(PropertiesFile properties, String key) {
        this.properties = properties
        this.key = key
    }

    String getValue() {
        properties[key]
    }

    void setValue(String value) {
        properties.put(key, value)
    }

    void apply(SemanticVersion.Increment increment) {
        def newSemanticVersion = semanticVersion.increment(increment)
        setValue(newSemanticVersion.toString())
    }

    private SemanticVersion getSemanticVersion() {
        return SemanticVersion.parse(getValue())
    }

    @Override
    String toString() {
        return value
    }
}
