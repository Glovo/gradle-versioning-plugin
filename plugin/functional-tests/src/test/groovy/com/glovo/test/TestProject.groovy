package com.glovo.test


import com.google.common.io.Resources
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner

class TestProject {

    private static final File ROOT_DIR = findRoot(new File(Resources.getResource('.').file))

    final File projectDir
    private final File appBuildScript

    static TestProject groovy(String name) {
        return create(name, BuildScriptTemplate.GROOVY)
    }

    static TestProject kotlin(String name) {
        return create(name, BuildScriptTemplate.KOTLIN)
    }

    private static TestProject create(String name, BuildScriptTemplate template) {
        def projectDir = createProjectDir(name)
        createSettingsScript(projectDir)
        createRootBuildScript(projectDir)
        createAppManifest(projectDir)
        def appBuild = createAppBuildScript(projectDir, template)

        def project = new TestProject(projectDir, appBuild)
        return project
    }

    private static File createProjectDir(String name) {
        def projectDir = new File(ROOT_DIR, "build/test-projects/$name")
        projectDir.deleteDir()
        projectDir.mkdirs()
        println("Created project dir at: $projectDir")
        return projectDir
    }

    private static File createSettingsScript(File projectDir) {
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
        println("Created settings script at $settings.path")
        return settings
    }

    private static File createRootBuildScript(File projectDir) {
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
        return build
    }

    private static File createAppManifest(File projectDir) {
        def manifest = new File(projectDir, 'app/src/main/AndroidManifest.xml')
        manifest.parentFile.mkdirs()
        manifest.text = """\
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android"
                      package="com.glovo.test">            
                <application />
            </manifest>
            """.stripIndent()
        return manifest
    }

    private static File createAppBuildScript(File projectDir, BuildScriptTemplate template) {
        def build = new File(projectDir, "app/$template.fileName")
        build.parentFile.mkdirs()
        build.text = template.content.stripIndent()
        build.text = build.text.stripIndent()
        return build
    }

    TestProject(File projectDir, File appBuildScript) {
        this.projectDir = projectDir
        this.appBuildScript = appBuildScript
    }

    BuildResult build(String... args) {
        return GradleRunner.create()
                .withProjectDir(projectDir)
                .withArguments(args)
                .forwardOutput()
                .build()
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
