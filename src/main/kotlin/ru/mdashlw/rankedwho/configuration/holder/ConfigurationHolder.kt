package ru.mdashlw.rankedwho.configuration.holder

import ru.mdashlw.rankedwho.configuration.ConfigurationManager

abstract class ConfigurationHolder {
    abstract val category: String

    val data = mutableMapOf<String, Any>()

    open fun onConfigurationLoad() = Unit

    fun register() {
        ConfigurationManager.holders += this
    }
}
