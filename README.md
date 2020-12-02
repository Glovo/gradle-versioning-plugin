# gradle-versioning-plugin

A Gradle plugin to unify the versioning workflow of Gradle builds.

## Features

- Automatic sync Gradle builds's `version` property.
- Automatic update of `versionName` and `versionCode` of an Android project

## Usage

For automatic `version` handling on any Gradle build add:

```kotlin
plugins {
    id("com.glovoapp.semantic-versioning") version < latest >
}
```

This will expect a `version.properties` file at root project containing the current version entry:

```properties
version=1.2.3
```

A `incrementSemanticVersion` task will be added to the build. When triggered (from CI for instance) it will increase the
version and update the file.

```shell
./gradlew incrementSemanticVersion
```

Will result on

```properties
version=1.2.4
```

You may specify also which version to increase: `--major`, `--minor` or `--path`

```shell
./gradlew incrementSemanticVersion --major
```

Will result on

```properties
version=2.0.0
```

### Android Version Name plugin

An Android extension is also available by applying:

```kotlin
plugins {
    id("com.glovoapp.android-versioning") version < latest >
}
```

This plugin will apply `com.glovoapp.semantic-versioning` and then also add a `incrementVersionCode` linked
to `versionCode` property in `version.properties`.

The behavior is the same, when run, the task will increment the `versionCode` by 1 (or by `--amount=XXX`)

## Releasing

At the moment this plugin is published in a private Artifactory repository.

To increment the version of the plugin to release modify the `version` property in the
root [`build.gradle.kts`](build.gradle.kts) file:

```kotlin
version = major.minor.patch
```

A new version of the artifacts can be released by running:

```
./gradlew publish
``` 

The above command will push the new version to the Artifactory provided you have the necessary credentials. This should
only be done from the CI.
