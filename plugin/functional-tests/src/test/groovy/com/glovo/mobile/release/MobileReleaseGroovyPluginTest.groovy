package com.glovo.mobile.release

import com.glovo.test.TestProject
import org.junit.Test

import static com.google.common.truth.Truth.assertThat

class MobileReleaseGroovyPluginTest {

    @Test
    void appliesPluginCorrectly() {
        def project = TestProject.groovy('simple-test-groovy')

        def result = project.build('clean')

        assertThat(result.output).contains('Applied com.glovo.mobile.release.MobileReleaseGroovyPlugin')
    }
}
