plugins {
    id("com.android.library")
}

android {
    compileSdkVersion(28)

    defaultConfig {
        minSdkVersion(19)
        targetSdkVersion(28)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}

dependencies {
    api("androidx.appcompat:appcompat:1.0.2")
}
