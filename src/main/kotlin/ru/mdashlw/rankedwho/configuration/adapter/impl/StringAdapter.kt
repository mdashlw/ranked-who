package ru.mdashlw.rankedwho.configuration.adapter.impl

import ru.mdashlw.rankedwho.configuration.adapter.ConfigurationAdapter

object StringAdapter : ConfigurationAdapter<String>() {
    override fun resolve(value: String): String = value
}
