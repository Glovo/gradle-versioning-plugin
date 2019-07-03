package com.glovo.mobile.release

import com.glovo.test.TestProject
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

import static com.google.common.truth.Truth.assertThat

@RunWith(Parameterized)
class IncrementVersionNameTest {

    @Parameterized.Parameters
    static def parameters() {
        return [
                { name -> createGroovyProject(name) },
                { name -> createKotlinProject(name) }
        ]
    }

    private final Closure<TestProject> createProject

    IncrementVersionNameTest(Closure<TestProject> createProject) {
        this.createProject = createProject
    }

    @Test
    void bumpsPatchVersionByDefault() {
        TestProject project = createProject('increment-version-name-default')
        def versions = project.newPropertiesFile('app/versions.properties', [versionName: '1.1.1'])

        project.build('incrementMainVersionName')

        assertThat(versions.text).contains('versionName=1.1.2')
    }

    @Test
    void bumpsMajorVersion() {
        TestProject project = createProject('increment-version-name-major')
        def versions = project.newPropertiesFile('app/versions.properties', [versionName: '1.1.1'])

        project.build('incrementMainVersionName', '--major')

        assertThat(versions.text).contains('versionName=2.0.0')
    }

    @Test
    void bumpsMinorVersion() {
        TestProject project = createProject('increment-version-name-minor')
        def versions = project.newPropertiesFile('app/versions.properties', [versionName: '1.1.1'])

        project.build('incrementMainVersionName', '--minor')

        assertThat(versions.text).contains('versionName=1.2.0')
    }

    @Test
    void bumpsPatchVersion() {
        TestProject project = createProject('increment-version-name-patch')
        def versions = project.newPropertiesFile('app/versions.properties', [versionName: '1.1.1'])

        project.build('incrementMainVersionName', '--patch')

        assertThat(versions.text).contains('versionName=1.1.2')
    }

    private static TestProject createGroovyProject(String projectName) {
        def project = TestProject.groovy("$projectName-groovy")
        project.appBuildScript += """\
                android {
                    defaultConfig {
                        persistedVersionName from: file('versions.properties')
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
                        persistedVersionName(from=file("versions.properties"))
                    }
                }
                """
        return project
    }

}
