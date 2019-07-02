package com.glovo.mobile.release

import com.glovo.test.TestProject
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith

import static com.google.common.truth.Truth.assertThat

@RunWith(Enclosed)
class PersistedVersionsTest {

    static class UsingGroovyScript {

        @Test
        void usesVersionNameAndVersionCodeFromSpecifiedPropertiedFile() {
            def project = TestProject.groovy('versions-groovy-1')
            project.newPropertiesFile('app/versions.properties', [versionName: '1.1.1', versionCode: 4321])
            project.appBuildScript += """\
                android {
                    defaultConfig {
                        persistedVersions from: file('versions.properties')
                    }
                }
                """

            project.build('assembleDebug')

            assertThat(project.generatedBuildConfig).contains('public static final String VERSION_NAME = "1.1.1";')
            assertThat(project.generatedBuildConfig).contains('public static final int VERSION_CODE = 4321;')
        }

        @Test
        void usesVersionNameFromSpecifiedPropertiedFile() {
            def project = TestProject.groovy('versions-groovy-2')
            project.newPropertiesFile('app/versions.properties', [versionName: '1.1.1', versionCode: 4321])
            project.appBuildScript += """\
                android {
                    defaultConfig {
                        persistedVersionName from: file('versions.properties')
                        versionCode 1
                    }
                }
                """

            project.build('assembleDebug')

            assertThat(project.generatedBuildConfig).contains('public static final String VERSION_NAME = "1.1.1";')
            assertThat(project.generatedBuildConfig).contains('public static final int VERSION_CODE = 1;')
        }

        @Test
        void usesVersionCodeFromSpecifiedPropertiesFile() {
            def project = TestProject.groovy('versions-groovy-3')
            project.newPropertiesFile('app/versions.properties', [versionName: '1.1.1', versionCode: 4321])
            project.appBuildScript += """\
                android {
                    defaultConfig {
                        versionName '0.0.0'
                        persistedVersionCode from: file('versions.properties')
                    }
                }
                """

            project.build('assembleDebug')

            assertThat(project.generatedBuildConfig).contains('public static final String VERSION_NAME = "0.0.0";')
            assertThat(project.generatedBuildConfig).contains('public static final int VERSION_CODE = 4321;')
        }

        @Test
        void allowsToOverrideVersionName() {
            def project = TestProject.groovy('versions-groovy-2')
            project.newPropertiesFile('app/versions.properties', [versionName: '1.1.1', versionCode: 4321])
            project.appBuildScript += """\
                android {
                    defaultConfig {
                        persistedVersionName from: file('versions.properties')
                        versionName '0.0.0'
                        versionCode 1
                    }
                }
                """

            project.build('assembleDebug')

            assertThat(project.generatedBuildConfig).contains('public static final String VERSION_NAME = "0.0.0";')
            assertThat(project.generatedBuildConfig).contains('public static final int VERSION_CODE = 1;')
        }

        @Test
        void allowsToOverrideVersionCode() {
            def project = TestProject.groovy('versions-groovy-3')
            project.newPropertiesFile('app/versions.properties', [versionName: '1.1.1', versionCode: 4321])
            project.appBuildScript += """\
                android {
                    defaultConfig {
                        versionName '0.0.0'
                        persistedVersionCode from: file('versions.properties')
                        versionCode 1234
                    }
                }
                """

            project.build('assembleDebug')

            assertThat(project.generatedBuildConfig).contains('public static final String VERSION_NAME = "0.0.0";')
            assertThat(project.generatedBuildConfig).contains('public static final int VERSION_CODE = 1234;')
        }

        @Test
        void doesNotRequireUsingPropertiesFiles() {
            def project = TestProject.groovy('versions-groovy-3')
            project.newPropertiesFile('app/versions.properties', [versionName: '1.1.1', versionCode: 4321])
            project.appBuildScript += """\
                android {
                    defaultConfig {
                        versionName '0.0.0'
                        versionCode 1234
                    }
                }
                """

            project.build('assembleDebug')

            assertThat(project.generatedBuildConfig).contains('public static final String VERSION_NAME = "0.0.0";')
            assertThat(project.generatedBuildConfig).contains('public static final int VERSION_CODE = 1234;')
        }
    }

    static class UsingKotlinScript {

        @Test
        void usesVersionNameAndVersionCodeFromSpecifiedPropertiesFile() {
            def project = TestProject.kotlin('versions-kotlin-1')
            project.newPropertiesFile('app/versions.properties', [versionName: '1.1.1', versionCode: 4321])
            project.appBuildScript += """\
                android {
                    defaultConfig {
                        persistedVersions(from=file("versions.properties"))
                    }
                }
                """

            project.build('assembleDebug')

            assertThat(project.generatedBuildConfig).contains('public static final String VERSION_NAME = "1.1.1";')
            assertThat(project.generatedBuildConfig).contains('public static final int VERSION_CODE = 4321;')
        }

        @Test
        void usesVersionNameFromSpecifiedPropertiesFile() {
            def project = TestProject.kotlin('versions-kotlin-2')
            project.newPropertiesFile('app/versions.properties', [versionName: '1.1.1', versionCode: 4321])
            project.appBuildScript += """\
                android {
                    defaultConfig {
                        persistedVersionName(from=file("versions.properties"))
                        versionCode = 1
                    }
                }
                """

            project.build('assembleDebug')

            assertThat(project.generatedBuildConfig).contains('public static final String VERSION_NAME = "1.1.1";')
            assertThat(project.generatedBuildConfig).contains('public static final int VERSION_CODE = 1;')
        }

        @Test
        void usesVersionCodeFromSpecifiedPropertiesFile() {
            def project = TestProject.kotlin('versions-kotlin-3')
            project.newPropertiesFile('app/versions.properties', [versionName: '1.1.1', versionCode: 4321])
            project.appBuildScript += """\
                android {
                    defaultConfig {
                        versionName = "0.0.0"
                        persistedVersionCode(from=file("versions.properties"))
                    }
                }
                """

            project.build('assembleDebug')

            assertThat(project.generatedBuildConfig).contains('public static final String VERSION_NAME = "0.0.0";')
            assertThat(project.generatedBuildConfig).contains('public static final int VERSION_CODE = 4321;')
        }

        @Test
        void allowsToOverrideVersionName() {
            def project = TestProject.kotlin('versions-kotlin-2')
            project.newPropertiesFile('app/versions.properties', [versionName: '1.1.1', versionCode: 4321])
            project.appBuildScript += """\
                android {
                    defaultConfig {
                        persistedVersionName(from=file("versions.properties"))
                        versionName = "0.0.0"
                        versionCode = 1
                    }
                }
                """

            project.build('assembleDebug')

            assertThat(project.generatedBuildConfig).contains('public static final String VERSION_NAME = "0.0.0";')
            assertThat(project.generatedBuildConfig).contains('public static final int VERSION_CODE = 1;')
        }

        @Test
        void allowsToOverrideVersionCode() {
            def project = TestProject.kotlin('versions-kotlin-3')
            project.newPropertiesFile('app/versions.properties', [versionName: '1.1.1', versionCode: 4321])
            project.appBuildScript += """\
                android {
                    defaultConfig {
                        versionName = "0.0.0"
                        persistedVersionCode(from=file("versions.properties"))
                        versionCode = 1234
                    }
                }
                """

            project.build('assembleDebug')

            assertThat(project.generatedBuildConfig).contains('public static final String VERSION_NAME = "0.0.0";')
            assertThat(project.generatedBuildConfig).contains('public static final int VERSION_CODE = 1234;')
        }

        @Test
        void doesNotRequireUsingPropertiesFiles() {
            def project = TestProject.groovy('versions-groovy-3')
            project.newPropertiesFile('app/versions.properties', [versionName: '1.1.1', versionCode: 4321])
            project.appBuildScript += """\
                android {
                    defaultConfig {
                        versionName = "0.0.0"
                        versionCode = 1234
                    }
                }
                """

            project.build('assembleDebug')

            assertThat(project.generatedBuildConfig).contains('public static final String VERSION_NAME = "0.0.0";')
            assertThat(project.generatedBuildConfig).contains('public static final int VERSION_CODE = 1234;')
        }
    }

}
