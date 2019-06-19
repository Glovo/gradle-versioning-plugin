package com.glovo.mobile.release

import com.glovo.mobile.release.internal.BaseMobileReleasePlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class MobileReleaseKotlinScriptPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        BaseMobileReleasePlugin().apply(target)
        println("Applied ${javaClass.canonicalName}")
    }
}
