package com.glovo.test

import com.google.common.io.Resources
import org.gradle.api.Action

class TestProject {

    private static final File ROOT_DIR = findRoot(new File(Resources.getResource('.').file))

    final File projectDir

    static TestProject groovy(String name, Action<File> buildScriptConfig = {}) {
        return create(name, Template.GROOVY, buildScriptConfig)
    }

    private static TestProject create(String name, Template template, Action<File> buildScriptConfig) {
        def project = new TestProject(name)
        project.createManifest()
        project.createSettingsScript()
        project.createBuildScript(template, buildScriptConfig)
        return project
    }

    TestProject(String name) {
        this.projectDir = new File(ROOT_DIR, "build/test-projects/$name")
        this.projectDir.deleteDir()
        this.projectDir.mkdirs()
        println("Created project dir at: $projectDir")
    }

    private void createManifest() {
        def manifest = new File(projectDir, 'src/main/AndroidManifest.xml')
        manifest.parentFile.mkdirs()
        manifest.text = """\
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android"
                      package="com.glovo.test">            
                <application />
            </manifest>
            """.stripIndent()
    }

    private void createSettingsScript() {
        def settings = new File(projectDir, 'settings.gradle')
        settings.text = """\
            includeBuild("../../../../..") {
                dependencySubstitution {
                    substitute(module("com.glovo.mobile-release:gradle-plugin")).with(project(":plugin:core"))
                    substitute(module("com.glovo.mobile-release:gradle-plugin-kotlin")).with(project(":plugin:kotlin-extensions"))
                }
            }
            """.stripIndent()
    }

    private void createBuildScript(Template template, Action<File> config = {}) {
        def build = new File(projectDir, template.fileName)
        build.text = template.content.stripIndent()
        config.execute(build)
    }

    private static final File findRoot(File current) {
        if (!current.isDirectory()) {
            return findRoot(current.parentFile)
        }
        if (current.name == 'functional-tests') {
            return current
        }
        if (current.parentFile == null) {
            throw new IllegalArgumentException("No root dir found!")
        }
        return findRoot(current.parentFile)
    }
}
