import org.gradle.kotlin.dsl.DependencyHandlerScope

const val junitVersion = "5.7.0"

fun DependencyHandlerScope.junit5(configuration: String = "testImplementation") {
    configuration("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    configuration("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}
