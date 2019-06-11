package ru.mdashlw.rankedwho.configuration.adapter

import ru.mdashlw.rankedwho.configuration.ConfigurationManager

abstract class ConfigurationAdapter<out T : Any> {
    abstract fun resolve(value: String): T
}

inline fun <reified T : Any> ConfigurationAdapter<T>.register() {
    ConfigurationManager.adapters[T::class] = this
}
