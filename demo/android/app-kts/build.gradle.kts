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

}

dependencies {
    implementation(project(":common"))
}
