# Android application versioning support

This plugin provides a way to automate the modification of the version name/code for an Android app 
allowing to read and modify those values using a separate properties file.
Consumers can use the provided extensions to the Android Gradle Plugin DSL to easily integrate
this functionality in their existing build scripts (using either Groovy or Kotlin).

## Persisted `versionName`

The plugin exposes a `persistedVersionName` extension for any `BaseFlavor` configured by the
Android Gradle Plugin. Example:

```groovy
android {
    defaultConfig {
        // other config ...
        persistedVersionName from: file('versions.properties')
        versionCode 1
    }
}
```

where `versions.properties` will be a simple properties file that must contain a `versionName` entry, eg:

```properties
versionName=1.2.3
```

The plugin assumes that the version name of the app follows semantic versioning, and will fail if
that precondition is not satisfied.

### Automated increment of persisted `versionName`

When consumers resort to `persistedVersionName` they will also be able to invoke a task
that can help automating the update of the `versionName` entry in the file, eg:
```
./gradlew incrementMainVersionName --minor 
```

The task name follows the pattern `increment${FlavorName}VersionName`, where `FlavorName` is the name
of the flavor you are targeting (if `defaultConfig` then that is `Main`). Possible options for this task
include:

* `--major`: updates the major segment, eg: `1.2.3` -> `2.0.0`
* `--minor`: updates the minor segment, eg: `1.2.3` -> `1.3.0`
* `--patch`: updates the patch segment, eg: `1.2.3` -> `1.2.4`

If no option is provided, the task will increment the minor segment by default.

## Persisted `versionCode`

The plugin exposes a `persistedVersionCode` extension for any `BaseFlavor` configured by the
Android Gradle Plugin. Example:

```groovy
android {
    defaultConfig {
        // other config ...
        versionName '1.2.3'
        persistedVersionCode from: file('versions.properties')
    }
}
```

where `versions.properties` will be a simple properties file that must contain a `versionCode` entry, eg:

```properties
versionCode=123
```

The plugin assumes that the version code of the app is just an integer, and will fail if
that precondition is not satisfied.

### Automated increment of persisted `versionCode`

When consumers resort to `persistedVersionCode` they will also be able to invoke a task
that can help automating the update of the `versionCode` entry in the file, eg:
```
./gradlew incrementMainVersionCode 
```

The task name follows the pattern `increment${FlavorName}VersionCode`, where `FlavorName` is the name
of the flavor you are targeting (if `defaultConfig` then that is `Main`).
There are no additional options available for this task.

# Utilities

In case consumers wanted to use the same properties file to host both the persisted `versionName` 
and `versionCode` then they will be able to use the convenience extension `persistedVersions`, eg:

```groovy
android {
    defaultConfig {
        // other config ...
        persistedVersions from: file('versions.properties')
    }
}
```

This extension bundles together the features of `persistedVersionName` and `persistedVersionCode`,
including the tasks listed above.
