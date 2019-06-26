package com.glovo.test

import com.google.common.io.Resources
import org.gradle.api.Action

class TestProject {

    private static final File ROOT_DIR = findRoot(new File(Resources.getResource('.').file))

    final File projectDir

    static TestProject groovy(String name, Action<File> buildScriptConfig = {}) {
        return create(name, BuildScriptTemplate.GROOVY, buildScriptConfig)
    }

    static TestProject kotlin(String name, Action<File> buildScriptConfig = {}) {
        return create(name, BuildScriptTemplate.KOTLIN, buildScriptConfig)
    }

    private static TestProject create(String name, BuildScriptTemplate template, Action<File> buildScriptConfig) {
        def project = new TestProject(name)
        project.createSettingsScript()
        project.createRootBuildScript()
        project.createAppBuildScript(template, buildScriptConfig)
        project.createAppManifest()
        return project
    }

    TestProject(String name) {
        this.projectDir = new File(ROOT_DIR, "build/test-projects/$name")
        this.projectDir.deleteDir()
        this.projectDir.mkdirs()
        println("Created project dir at: $projectDir")
    }

    private void createSettingsScript() {
        def settings = new File(projectDir, 'settings.gradle.kts')
        settings.text = """\
            include(":app")
            
            includeBuild("../../../../..") {
                dependencySubstitution {
                    substitute(module("com.glovo.mobile-release:gradle-plugin")).with(project(":plugin:core"))
                    substitute(module("com.glovo.mobile-release:gradle-plugin-kotlin")).with(project(":plugin:kotlin-extensions"))
                }
            }
            """.stripIndent()
    }

    private void createRootBuildScript() {
        def build = new File(projectDir, 'build.gradle.kts')
        build.text = """\
            buildscript {
                repositories {
                    google()
                    mavenCentral()
                    jcenter()
                }
                dependencies {
                    classpath("com.android.tools.build:gradle:3.4.1")
                    classpath(kotlin("gradle-plugin", version = "1.3.31"))
                }
            }
            
            allprojects {
                repositories {
                    google()
                    mavenCentral()
                    jcenter()
                }
            }
            """.stripIndent()
    }

    private void createAppManifest() {
        def manifest = new File(projectDir, 'app/src/main/AndroidManifest.xml')
        manifest.parentFile.mkdirs()
        manifest.text = """\
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android"
                      package="com.glovo.test">            
                <application />
            </manifest>
            """.stripIndent()
    }

    private void createAppBuildScript(BuildScriptTemplate template, Action<File> config = {}) {
        def build = new File(projectDir, "app/$template.fileName")
        build.parentFile.mkdirs()
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
