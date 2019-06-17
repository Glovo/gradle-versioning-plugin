rootProject.name = "android-demo"

include(":app")

includeBuild("../../") {
    dependencySubstitution {
        substitute(module("com.glovo:gradle-mobile-release-plugin")).with(project(":plugin"))
    }
}
