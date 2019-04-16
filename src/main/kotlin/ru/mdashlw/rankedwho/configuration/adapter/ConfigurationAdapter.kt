package ru.mdashlw.rankedwho.configuration.adapter

import ru.mdashlw.rankedwho.configuration.ConfigurationManager
import kotlin.reflect.KClass

abstract class ConfigurationAdapter<out T : Any>(val type: KClass<out T>) {
    abstract fun resolve(value: String): T

    fun register() {
        ConfigurationManager.adapters += this
    }
}
