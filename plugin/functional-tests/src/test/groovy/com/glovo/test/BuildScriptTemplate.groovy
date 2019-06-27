package com.glovo.test

enum BuildScriptTemplate {
    GROOVY('build.gradle', GROOVY_CONTENT),
    KOTLIN('build.gradle.kts', KOTLIN_CONTENT)

    final String fileName
    final String content

    BuildScriptTemplate(String fileName, String content) {
        this.fileName = fileName
        this.content = content
    }

    private static final String GROOVY_CONTENT = """\
        buildscript {
            repositories {
                google()
                mavenCentral()
            }
            dependencies {
                classpath 'com.android.tools.build:gradle:3.4.1'
                classpath 'com.glovo.mobile-release:gradle-plugin:local'
            }
        }
        
        repositories {
            google()
            mavenCentral()
            jcenter()
        }
        
        apply plugin: 'com.android.application'
        apply plugin: 'com.glovo.mobile-release'
        
        android {
            compileSdkVersion 28
        
            defaultConfig {
                minSdkVersion 19
                targetSdkVersion 28
                multiDexEnabled = true
            }
        
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }
        
        }
        
        """

    private static final String KOTLIN_CONTENT = """\
        plugins {
            id("com.android.application")
        }
        
        buildscript {
            repositories {
                google()
                mavenCentral()
            }
            dependencies {
                classpath ("com.android.tools.build:gradle:3.4.1")
                classpath("com.glovo.mobile-release:gradle-plugin-kotlin:local")
            }
        }
        
        repositories {
            google()
            mavenCentral()
            jcenter()
        }
        
        apply(plugin = "com.glovo.mobile-release-kotlin")
        
        android {
            compileSdkVersion(28)
        
            defaultConfig {
                minSdkVersion(19)
                targetSdkVersion(28)
                multiDexEnabled = true
            }
        
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }
        }
        
        """
}