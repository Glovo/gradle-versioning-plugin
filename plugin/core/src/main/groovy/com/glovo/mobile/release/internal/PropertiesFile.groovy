package com.glovo.mobile.release.internal

class PropertiesFile {

    private final File file
    private final Closure<Properties> propertiesProvider

    PropertiesFile(File file) {
        this.file = file
        this.propertiesProvider = {
            if (!file.exists()) {
                throw new FileNotFoundException("File '$file.path' not found")
            }
            def properties = new Properties()
            properties.load(new FileInputStream(file))
            return properties
        }.memoize()
    }

    private Properties getProperties() {
        propertiesProvider.call()
    }

    String getAt(String key) {
        String value = properties[key]
        if (value == null) {
            throw new NullPointerException("No value found for '$key'")
        } else {
            return value
        }
    }

    void put(String key, String value) {
        properties[key] = value
        def stream = file.newOutputStream()
        properties.store(stream, '')
        stream.close()
    }

    boolean contains(String key) {
        return properties[key] != null
    }
}
