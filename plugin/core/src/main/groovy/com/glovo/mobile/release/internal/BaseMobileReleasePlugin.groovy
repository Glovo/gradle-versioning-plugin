package com.glovo.mobile.release.internal

import org.gradle.api.Plugin
import org.gradle.api.Project

class BaseMobileReleasePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        println("Applied ${this.class.canonicalName}")
    }
}