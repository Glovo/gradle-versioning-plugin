package com.glovo.mobile.release

import com.glovo.test.rules.TemporaryFolder
import org.junit.Rule
import org.junit.Test

import static com.google.common.truth.Truth.assertThat

class PersistedNumericVersionTest {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder()

    @Test
    void readsValueFromFile() {
        def file = temp.newFile('version=10')

        def version = PersistedNumericVersion.from(file, 'version')

        assertThat(version.value).isEqualTo(10)
    }

    @Test
    void writesValueToFile() {
        def file = temp.newFile('version=10')
        def version = PersistedNumericVersion.from(file, 'version')

        version.value = 100

        assertThat(file.text).contains('version=100')
    }

    @Test
    void incrementsValue() {
        def file = temp.newFile('version=10')
        def version = PersistedNumericVersion.from(file, 'version')

        version.increment()

        assertThat(file.text).contains('version=11')
    }

}
