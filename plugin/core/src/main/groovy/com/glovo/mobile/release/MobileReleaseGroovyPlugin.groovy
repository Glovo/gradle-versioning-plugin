package com.glovo.mobile.release

import com.glovo.mobile.release.internal.PropertiesFile
import com.glovo.mobile.release.dsl.BaseFlavorGroovyDSL
import org.gradle.api.Plugin
import org.gradle.api.Project

class MobileReleaseGroovyPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println("Applied ${this.class.canonicalName}")

        project.ext.propertiesFile = { path -> new PropertiesFile(project.file(path)) }

        project.plugins.withId('com.android.application') {
            BaseFlavorGroovyDSL.extend(project.android.defaultConfig, project)
            project.android.productFlavors.all {
                BaseFlavorGroovyDSL.extend(it, project)
            }
        }
    }

}
