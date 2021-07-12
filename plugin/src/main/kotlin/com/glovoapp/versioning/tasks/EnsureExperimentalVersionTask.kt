package com.glovoapp.versioning.tasks

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

open class EnsureExperimentalVersionTask : IncrementSemanticVersionTask() {

    init {
        amount.value(0)
        preReleaseLabels.convention(
            listOf("dev-experimental-${LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)}")
        )
    }

}
