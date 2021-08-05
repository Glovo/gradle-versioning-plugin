package com.glovoapp.versioning

import org.gradle.api.provider.Provider

/**
 * A helper class to be able to lazy provide [org.gradle.api.Project.getVersion]
 */
internal class ToStringProviderWrapper<T>(
    private val wrapped: Provider<T>
) : Provider<T> by wrapped {

    override fun toString() = wrapped.orNull.toString()

}
