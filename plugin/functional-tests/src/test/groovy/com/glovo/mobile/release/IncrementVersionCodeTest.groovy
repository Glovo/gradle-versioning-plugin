package com.glovo.mobile.release

import com.glovo.test.TestProject
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

import static com.google.common.truth.Truth.assertThat

@RunWith(Parameterized)
class IncrementVersionCodeTest {

    @Parameterized.Parameters
    static def parameters() {
        return [
                { name -> createGroovyProject(name) },
                { name -> createKotlinProject(name) }
        ]
    }

    private final Closure<TestProject> createProject

    IncrementVersionCodeTest(Closure<TestProject> createProject) {
        this.createProject = createProject
    }

    @Test
    void bumpsVersionCode() {
        TestProject project = createProject('increment-version-code')
        def versions = project.newPropertiesFile('app/versions.properties', [versionCode: '11'])

        project.build('incrementMainVersionCode')

        assertThat(versions.text).contains('versionCode=12')
    }

    private static TestProject createGroovyProject(String projectName) {
        def project = TestProject.groovy("$projectName-groovy")
        project.appBuildScript += """\
                android {
                    defaultConfig {
                        persistedVersionCode from: file('versions.properties')
                    }
                }
                """
        return project
    }

    private static TestProject createKotlinProject(String projectName) {
        def project = TestProject.kotlin("$projectName-kotlin")
        project.appBuildScript += """\
                android {
                    defaultConfig {
                        persistedVersionCode(from=file("versions.properties"))
                    }
                }
                """
        return project
    }

}
