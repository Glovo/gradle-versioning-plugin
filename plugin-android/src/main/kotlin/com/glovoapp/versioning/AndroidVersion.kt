package com.glovoapp.versioning

data class AndroidVersion(
    val code: PersistedVersion<Int>?,
    val name: PersistedVersion<SemanticVersion>?
) {

    override fun toString() = (name ?: code)?.toString() ?: super.toString()

}
