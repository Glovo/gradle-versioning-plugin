package com.glovo.mobile.release


import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskAction

@SuppressWarnings("UnstableApiUsage")
class IncrementPersistedNumericVersionTask extends DefaultTask {

    final Property<PersistedNumericVersion> versionCode = project.objects.property(PersistedNumericVersion)

    IncrementPersistedNumericVersionTask() {
        group = 'release'
    }

    @TaskAction
    void run() {
        versionCode.get().increment()
    }
}
