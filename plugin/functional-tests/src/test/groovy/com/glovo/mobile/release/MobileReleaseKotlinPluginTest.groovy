package com.glovo.mobile.release

import com.glovo.test.TestProject
import org.junit.Test

import static com.google.common.truth.Truth.assertThat

class MobileReleaseKotlinPluginTest {

    @Test
    void appliesPluginCorrectly() {
        def project = TestProject.kotlin('simple-test-kotlin')

        def result = project.build('clean')

        assertThat(result.output).contains('Applied com.glovo.mobile.release.MobileReleaseKotlinScriptPlugin')
    }
}
