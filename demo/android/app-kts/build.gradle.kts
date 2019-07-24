plugins {
    id("com.android.application")
}

buildscript {
    dependencies {
        classpath("com.glovo.mobile-release:gradle-plugin-kotlin:local")
    }
}

apply(plugin = "com.glovo.mobile-release-kotlin")

android {

    compileSdkVersion(28)

    defaultConfig {
        minSdkVersion(19)
        targetSdkVersion(28)
        applicationId = "com.glovo.demo.release.kotlin"
        multiDexEnabled = true

        persistedVersionName(from = file("../android.properties"))
        versionCode = 4321
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    signingConfigs {
        maybeCreate("debug").apply {
            storeFile = rootProject.file("debug.jks")
            storePassword = "android"
            keyAlias = "debug"
            keyPassword = "android"
        }
        maybeCreate("release").apply {
            storeFile = rootProject.file("release.jks")
            storePassword = "android"
            keyAlias = "release"
            keyPassword = "android"
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs["debug"]
        }
        getByName("release") {
            signingConfig = signingConfigs["release"]
        }
    }

}

dependencies {
    implementation(project(":common"))
}
