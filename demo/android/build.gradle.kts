buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.4.1")
        classpath(kotlin("gradle-plugin", version = "1.3.31"))
        classpath("com.glovo:gradle-mobile-release-plugin:local")
    }
}

repositories {
    google()
    mavenCentral()
    jcenter()
}
