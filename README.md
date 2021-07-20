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

Once applied, the `Project.version` will be set to an instance of `PersistedVersion<SemanticVersion>`. 
It's also accessible from the property `semanticVersion`:
```kotlin

task("printVersion") {
    doLast {
        pritnln("The project current version is ${project.semanticVersion}")
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

Once applied, the `Project.version` will be set to an instance of `AndroidVersion`.
It's also accessible from the property `androidVersion`:
```kotlin

task("printVersion") {
    doLast {
        pritnln("The project current versionCode is ${project.androidVersion.code} and name is ${project.androidVersion.name}")
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
