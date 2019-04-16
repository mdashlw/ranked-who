package ru.mdashlw.rankedwho.configuration.holder.impl

import ru.mdashlw.hypixel.HypixelAPI
import ru.mdashlw.rankedwho.configuration.holder.ConfigurationHolder
import ru.mdashlw.rankedwho.configuration.property.ConfigurationProperty
import ru.mdashlw.util.uuid.EMPTY_UUID

object MainConfiguration : ConfigurationHolder() {
    override val category: String = "main"

    var apiKey by ConfigurationProperty(EMPTY_UUID) {
        HypixelAPI.apiKey = it
    }

    override fun onConfigurationLoad() {
        HypixelAPI.apiKey = apiKey
    }
}
