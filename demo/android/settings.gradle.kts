rootProject.name = "android-demos"

include(":app-kts", ":app")

includeBuild("../../") {
    dependencySubstitution {
        substitute(module("com.glovo.mobile-release:gradle-plugin")).with(project(":plugin:core"))
        substitute(module("com.glovo.mobile-release:gradle-plugin-kotlin")).with(project(":plugin:kotlin-extensions"))
    }
}
