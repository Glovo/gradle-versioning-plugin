[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](./LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/com.glovoapp.gradle/versioning)](https://search.maven.org/artifact/com.glovoapp.gradle/versioning)
[![Gradle Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/com.glovoapp.semantic-versioning)](https://plugins.gradle.org/plugin/com.glovoapp.semantic-versioning)
![Build Status](https://github.com/Glovo/gradle-versioning-plugin/actions/workflows/build.yaml/badge.svg)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Glovo_gradle-versioning-plugin&metric=coverage&token=6b5b2b8c32bc6be61f60223590e3d1be371ac0fb)](https://sonarcloud.io/dashboard?id=Glovo_gradle-versioning-plugin)
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

Once applied, the `Project.version` will be set with the given version 
which can it also be accessed from `semanticVersion.version` property:
```kotlin

task("printVersion") {
    doLast {
        pritnln("The project current version is ${project.semanticVersion.version.get()}")
    }
}
```

The plugin requires a `version.properties` file at root project containing the current version entry:
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

Once applied, version can access from the `android.versioning.version` property:
```kotlin

task("printVersion") {
    doLast {
        val androidVersion = project.android.versioning.version.get()
        pritnln("The project current versionCode is ${androidVersion.code} and name is ${androidVersion.name}")
    }
}
```

The plugin requires a `version.properties` file at root project containing the at least one of the following entries:
```properties
versionCode=10
versionName=1.2.3
```

If `versionCode` is set, and `incrementVersionCode` task will be added to the project.
The behavior is the same, when run, the task will increment the `versionCode` by 1 (or by `--amount=XXX`)

If `versionName` is set, and `incrementVersionName` task will be added to the project.
The behavior is exactly the same as the `incrementSemanticVersion` task for non-Android projects.
