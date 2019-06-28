package com.glovo.mobile.release.internal


import java.util.regex.Pattern

class SemanticVersion {

    enum Increment {

        MAJOR({ SemanticVersion v -> new SemanticVersion(v.major + 1, 0, 0) }),
        MINOR({ SemanticVersion v -> new SemanticVersion(v.major, v.minor + 1, 0) }),
        PATCH({ SemanticVersion v -> new SemanticVersion(v.major, v.minor, v.patch + 1) })

        final def transform

        Increment(def transform) {
            this.transform = transform
        }
    }

    private static final Pattern VERSION_PATTERN = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)")

    final int major
    final int minor
    final int patch

    static SemanticVersion parse(String source) {
        def matcher = VERSION_PATTERN.matcher(source)
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Unsupported format for semantic version: $source")
        }
        def major = Integer.parseInt(matcher.group(1))
        def minor = Integer.parseInt(matcher.group(2))
        def patch = Integer.parseInt(matcher.group(3))
        new SemanticVersion(major, minor, patch)
    }

    SemanticVersion(int major, int minor, int patch) {
        this.major = major
        this.minor = minor
        this.patch = patch
    }

    SemanticVersion increment(Increment increment) {
        return increment.transform(this)
    }

    @Override
    String toString() {
        return "$major.$minor.$patch"
    }

    boolean equals(o) {
        if (this.is(o)) {
            return true
        }
        if (getClass() != o.class) {
            return false
        }

        SemanticVersion that = (SemanticVersion) o

        if (major != that.major) {
            return false
        }
        if (minor != that.minor) {
            return false
        }
        if (patch != that.patch) {
            return false
        }

        return true
    }

    int hashCode() {
        int result
        result = major
        result = 31 * result + minor
        result = 31 * result + patch
        return result
    }
}
