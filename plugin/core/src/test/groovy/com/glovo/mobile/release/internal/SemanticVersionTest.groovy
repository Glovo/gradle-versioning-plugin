package com.glovo.mobile.release.internal


import org.junit.Test

import static com.glovo.mobile.release.internal.SemanticVersion.Increment.*
import static com.google.common.truth.Truth.assertThat
import static org.junit.Assert.fail

class SemanticVersionTest {

    @Test
    void throwsWhenParsingWrongFormat() {
        try {
            SemanticVersion.parse('x.y.z')

            fail('Exception expected but not thrown')
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException)
        }
    }

    @Test
    void parsesCorrectFormat() {
        def version = SemanticVersion.parse('1.2.3')

        assertThat(version.major).isEqualTo(1)
        assertThat(version.minor).isEqualTo(2)
        assertThat(version.patch).isEqualTo(3)
    }

    @Test
    void printsUsingCorrectFormat() {
        def text = '1.2.3'

        def version = SemanticVersion.parse(text)

        assertThat(version.toString()).isEqualTo(text)
    }

    @Test
    void incrementsMajorCorrectly() {
        def version = SemanticVersion.parse('1.2.3')

        def newVersion = version.increment(MAJOR)

        assertThat(newVersion.toString()).isEqualTo('2.0.0')
    }

    @Test
    void incrementsMinorCorrectly() {
        def version = SemanticVersion.parse('1.2.3')

        def newVersion = version.increment(MINOR)

        assertThat(newVersion.toString()).isEqualTo('1.3.0')
    }

    @Test
    void incrementsPatchCorrectly() {
        def version = SemanticVersion.parse('1.2.3')

        def newVersion = version.increment(PATCH)

        assertThat(newVersion.toString()).isEqualTo('1.2.4')
    }
}