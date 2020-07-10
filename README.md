# gradle-mobile-release-plugin

A Gradle plugin to unify the release workflow of mobile apps.

## Features

- Supports automated update of `versionName` and `versionCode` of an Android app: [how to use persisted versions](/docs/AndroidVersions.md)

## Downloading

Plugin is currently hosted on Glovo Artifactory repository. For plugin to be used, set up Artifactory credentials on your dev machine, as explained in [the official documentation](https://github.com/Glovo/gradle-artifactory-plugin#user-local-setup).

## Releasing

At the moment this plugin is published in a private Artifactory repository.

To increment the version of the plugin to release modify the `version` property in the root [`build.gradle`](build.gradle) file:
```groovy
version = major.minor.patch
```

A new version of the artifacts can be released by running:
```
./gradlew publish
``` 
The above command will push the new version to the Artifactory provided you have the necessary credentials. This should only be done from the CI.
