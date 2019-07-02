package com.glovo.mobile.release

import com.glovo.mobile.release.internal.PropertiesFile

class PersistedNumericVersion {

    private final PropertiesFile properties
    private final String key

    static PersistedNumericVersion from(File file, String key) {
        return new PersistedNumericVersion(new PropertiesFile(file), key)
    }

    private PersistedNumericVersion(PropertiesFile properties, String key) {
        this.properties = properties
        this.key = key
    }

    int getValue() {
        properties[key] as int
    }

    void setValue(int value) {
        properties.put(key, "$value")
    }

    void increment() {
        value++
    }

    @Override
    String toString() {
        return "$value"
    }
}
