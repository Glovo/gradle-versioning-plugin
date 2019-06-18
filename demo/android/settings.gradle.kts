rootProject.name = "android-demos"

include(":app-kts")

includeBuild("../../") {
    dependencySubstitution {
        substitute(module("com.glovo.mobile-release:gradle-plugin")).with(project(":plugin"))
    }
}
