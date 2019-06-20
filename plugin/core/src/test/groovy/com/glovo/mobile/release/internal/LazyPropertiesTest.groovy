package com.glovo.mobile.release.internal


import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import static com.google.common.truth.Truth.assertThat
import static org.junit.Assert.fail

class LazyPropertiesTest {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder()

    @Test
    void doesNotThrowEagerlyWhenFileNotFound() {
        File file = new File(temp.root.path, 'notThere.properties')

        new LazyProperties(file)
    }

    @Test
    void throwsWhenFileNotFoundAsSoonPropertiesAccessed() {
        File file = new File(temp.root.path, 'notThere.properties')

        def properties = new LazyProperties(file)

        try {
            properties['foo']

            fail('FileNotFoundException expected but not thrown')
        } catch (Exception e) {
            assertThat(e).isInstanceOf(FileNotFoundException)
        }
    }

    @Test
    void returnsValueForSpecifiedKey() {
        def file = newPropertiesFile('one=1')

        def properties = new LazyProperties(file)

        assertThat(properties['one']).isEqualTo('1')
    }

    @Test
    void updatesValueForSpecifiedKey() {
        def file = newPropertiesFile('one=1')
        def properties = new LazyProperties(file)

        properties.put('one', 'uno')

        assertThat(properties['one']).isEqualTo('uno')
    }

    @Test
    void persistsUpdatedValueForSpecifiedKey() {
        def file = newPropertiesFile('one=1')
        def properties = new LazyProperties(file)

        properties.put('one', 'uno')

        assertThat(file.text).contains('one=uno')
    }

    private File newPropertiesFile(String content) {
        def file = temp.newFile()
        file.write(content)
        file
    }
}
