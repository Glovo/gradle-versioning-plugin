package com.glovo.mobile.release

import com.glovo.mobile.release.internal.SemanticVersion
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

@SuppressWarnings("UnstableApiUsage")
class IncrementPersistedSemanticVersionTask extends DefaultTask {

    final Property<PersistedSemanticVersion> versionName = project.objects.property(PersistedSemanticVersion)

    private SemanticVersion.Increment increment = SemanticVersion.Increment.PATCH

    @Option(option = 'major', description = 'Increments major version')
    void major() {
        increment = SemanticVersion.Increment.MAJOR
    }

    @Option(option = 'minor', description = 'Increments minor version')
    void minor() {
        increment = SemanticVersion.Increment.MINOR
    }

    @Option(option = 'patch', description = 'Increments patch version')
    void patch() {
        increment = SemanticVersion.Increment.PATCH
    }

    IncrementPersistedSemanticVersionTask() {
        group = 'release'
    }

    @TaskAction
    void run() {
        versionName.get().apply(increment)
    }
}
