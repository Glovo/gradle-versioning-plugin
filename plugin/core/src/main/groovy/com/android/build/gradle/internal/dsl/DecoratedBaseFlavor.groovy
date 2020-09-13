package com.android.build.gradle.internal.dsl

import com.glovo.mobile.release.IncrementPersistedNumericVersionTask
import com.glovo.mobile.release.IncrementPersistedSemanticVersionTask
import com.glovo.mobile.release.PersistedNumericVersion
import com.glovo.mobile.release.PersistedSemanticVersion
import com.glovo.mobile.release.internal.PropertiesFile
import org.gradle.api.Project

class DecoratedBaseFlavor {

    final BaseFlavor base

    final Project project

    DecoratedBaseFlavor(BaseFlavor base, Project project) {
        this.base = base
        this.project = project
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
        def versionName = new PersistedSemanticVersion(properties, 'versionName')
        base.setVersionName(versionName.value)
        configureVersionNameTask(versionName)
    }

    private void configureVersionNameTask(PersistedSemanticVersion versionName) {
        def taskName = "increment${base.name.capitalize()}VersionName"
        def task = project.tasks.findByName(taskName)
        if (task == null) {
            project.tasks.register(taskName, IncrementPersistedSemanticVersionTask) {
                it.versionName.value(versionName)
            }
        } else {
            (task as IncrementPersistedSemanticVersionTask).versionName.value(versionName)
        }
    }

    void setPersistedVersionCode(def source) {
        PropertiesFile properties = propertiesFrom(source)
        def versionCode = new PersistedNumericVersion(properties, 'versionCode')
        base.setVersionCode(versionCode.value)
        configureVersionCodeTask(versionCode)
    }

    private void configureVersionCodeTask(PersistedNumericVersion versionCode) {
        def taskName = "increment${base.name.capitalize()}VersionCode"
        def task = project.tasks.findByName(taskName)
        if (task == null) {
            project.tasks.register(taskName, IncrementPersistedNumericVersionTask) {
                it.versionCode.value(versionCode)
            }
        } else {
            (task as IncrementPersistedNumericVersionTask).versionCode.value(versionCode)
        }
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
