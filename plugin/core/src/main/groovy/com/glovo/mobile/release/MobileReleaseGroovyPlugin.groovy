package com.glovo.mobile.release


import org.gradle.api.Plugin
import org.gradle.api.Project

class MobileReleaseGroovyPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        println("Applied ${this.class.canonicalName}")
    }
}
