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
        applicationId = "com.glovo.test.demo"
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}
