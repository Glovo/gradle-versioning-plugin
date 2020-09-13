package com.glovo.mobile.release.dsl

import com.android.build.gradle.internal.dsl.BaseFlavor
import com.android.build.gradle.internal.dsl.DecoratedBaseFlavor
import org.gradle.api.Project

class BaseFlavorGroovyDSL {

    private final DecoratedBaseFlavor decorated

    static void extend(BaseFlavor base, Project project) {
        def dsl = new BaseFlavorGroovyDSL(new DecoratedBaseFlavor(base, project))
        base.ext.persistedVersions = { source -> dsl.persistedVersions(source) }
        base.ext.persistedVersionName = { source -> dsl.persistedVersionName(source) }
        base.ext.persistedVersionCode = { source -> dsl.persistedVersionCode(source) }
    }

    private BaseFlavorGroovyDSL(DecoratedBaseFlavor decorated) {
        this.decorated = decorated
    }

    void persistedVersions(Map<String, ?> options) {
        decorated.setPersistedVersionsFrom(options['from'])
    }

    void persistedVersionName(Map<String, ?> options) {
        decorated.setPersistedVersionName(options['from'])
    }

    void persistedVersionCode(Map<String, ?> options) {
        decorated.setPersistedVersionCode(options['from'])
    }
}
