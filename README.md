# gradle-mobile-release-plugin

A Gradle plugin to unify the release workflow of mobile apps.

## Features

- Supports automated update of `versionName` and `versionCode` of an Android app: [how to use persisted versions](/docs/AndroidVersions.md)

## Downloading

In order to download the plugin from the private bucket, you need:

1. An AWS account that can access the S3 bucket with id `android-artifact-repository-main-bucket-bucket-124ev9rdkbskl`.
If that is not the case then you should submit a ticket to the platform team using this form: https://glovoapp.atlassian.net/servicedesk/customer/portal/26
 
2. Set up the AWS CLI, as explained [here](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html), and configure it, as shown [here](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-configure.html#cli-quick-configuration).

3. Add the S3 bucket as an additional repository in your project, as follows:

```groovy
buildscript {
    repositories {
        maven {
            url "s3://android-artifact-repository-main-bucket-bucket-124ev9rdkbskl/maven"
            authentication {
                awsIm(AwsImAuthentication)
            }
        }
    }
    dependencies {
        classpath 'com.glovo.gradle:android-version-plugin:0.0.1'
    }
}
```

## Releasing

At the moment this plugin is published to a private AWS S3 bucket. A new version of the artifacts can be released by running:
```
./gradlew publish
``` 
The above command will push the new version to the S3 bucket provided you have the necessary credentials. This should only be done from the CI.
