package ru.mdashlw.rankedwho

import ru.mdashlw.hypixel.api.HypixelApi
import ru.mdashlw.rankedwho.configuration.ConfigurationHolder
import ru.mdashlw.rankedwho.configuration.ConfigurationProperty

object MainConfiguration : ConfigurationHolder() {
    override val category: String = "main"

    var toggled by ConfigurationProperty(true)
    var autoWho by ConfigurationProperty(true)
    var apiKey by ConfigurationProperty("") {
        updateKey(it)
    }

    override fun onConfigurationLoad() {
        updateKey(apiKey)
    }

    private fun updateKey(key: String) {
        if (key.isEmpty()) {
            HypixelApi.keys = emptyList()
        } else {
            HypixelApi.keys = listOf(key)
        }
    }
}
