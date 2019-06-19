package com.glovo.mobile.release

import com.glovo.mobile.release.internal.BaseMobileReleasePlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class MobileReleaseGroovyPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        new BaseMobileReleasePlugin().apply(target)
        println("Applied ${this.class.canonicalName}")
    }
}
