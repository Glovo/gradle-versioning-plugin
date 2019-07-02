package com.glovo.test.rules

class TemporaryFolder extends org.junit.rules.TemporaryFolder {

    TemporaryFolder() {
        this(null)
    }

    TemporaryFolder(File parentFolder) {
        super(parentFolder)
    }

    File newFile(String text) {
        def file = newFile()
        file.text = text
        return file
    }
}
