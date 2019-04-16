package ru.mdashlw.rankedwho.configuration.adapter.impl

import ru.mdashlw.rankedwho.configuration.adapter.ConfigurationAdapter
import java.util.*

object UuidAdapter : ConfigurationAdapter<UUID>(UUID::class) {
    override fun resolve(value: String): UUID = UUID.fromString(value)
}
