buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.1")
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
