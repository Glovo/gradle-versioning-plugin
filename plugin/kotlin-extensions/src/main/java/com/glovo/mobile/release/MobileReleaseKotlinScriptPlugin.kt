package com.glovo.mobile.release

import org.gradle.api.Plugin
import org.gradle.api.Project

class MobileReleaseKotlinScriptPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        println("Applied ${javaClass.canonicalName}")
    }
}
