package com.android.build.gradle.internal.dsl

import com.glovo.mobile.release.internal.PropertiesFile

class DecoratedBaseFlavor {

    final BaseFlavor base

    DecoratedBaseFlavor(BaseFlavor base) {
        this.base = base
    }

    void setPersistedVersionsFrom(def source) {
        PropertiesFile properties = propertiesFrom(source)
        if (properties.contains('versionName')) {
            setPersistedVersionName(properties)
        }
        if (properties.contains('versionCode')) {
            setPersistedVersionCode(properties)
        }
    }

    void setPersistedVersionName(def source) {
        PropertiesFile properties = propertiesFrom(source)
        base.setVersionName(properties['versionName'])
    }

    void setPersistedVersionCode(def source) {
        PropertiesFile properties = propertiesFrom(source)
        base.setVersionCode(properties['versionCode'] as int)
    }

    private static PropertiesFile propertiesFrom(def source) {
        if (source instanceof File) {
            return new PropertiesFile(source)
        } else if (source instanceof PropertiesFile) {
            return source
        } else {
            throw new IllegalArgumentException("Unsupported persisted versions source: $source")
        }
    }

}
