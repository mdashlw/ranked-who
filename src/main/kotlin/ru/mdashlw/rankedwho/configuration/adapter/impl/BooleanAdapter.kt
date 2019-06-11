package ru.mdashlw.rankedwho.configuration.adapter.impl

import ru.mdashlw.rankedwho.configuration.adapter.ConfigurationAdapter

object BooleanAdapter : ConfigurationAdapter<Boolean>() {
    override fun resolve(value: String): Boolean = value.toBoolean()
}
