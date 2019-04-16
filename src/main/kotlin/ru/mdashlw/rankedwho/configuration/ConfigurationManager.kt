package ru.mdashlw.rankedwho.configuration

import net.minecraftforge.common.config.Property
import ru.mdashlw.rankedwho.RankedWho
import ru.mdashlw.rankedwho.configuration.adapter.ConfigurationAdapter
import ru.mdashlw.rankedwho.configuration.adapter.impl.UuidAdapter
import ru.mdashlw.rankedwho.configuration.holder.ConfigurationHolder
import ru.mdashlw.rankedwho.configuration.holder.impl.MainConfiguration
import kotlin.reflect.KClass

object ConfigurationManager {
    val holders = mutableListOf<ConfigurationHolder>()
    val adapters = mutableListOf<ConfigurationAdapter<Any>>()

    fun registerMainConfiguration() {
        MainConfiguration.register()
    }

    fun registerDefaultAdapters() {
        UuidAdapter.register()
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(RuntimeException::class)
    private fun <T : Any> getConfigurationAdapter(type: KClass<out T>): ConfigurationAdapter<T> =
        adapters.find { it.type == type } as? ConfigurationAdapter<T>
            ?: throw RuntimeException("No configuration adapter for type ${type.qualifiedName}")

    fun load() {
        holders.forEach { holder ->
            val category = RankedWho.configuration.getCategory(holder.category)

            holder.data.forEach { (key, value) ->
                category[key]?.string
                    ?.let {
                        val adapter = getConfigurationAdapter(value::class)

                        holder.data[key] = adapter.resolve(it)
                    }
            }

            holder.onConfigurationLoad()
        }
    }

    fun save() {
        holders.forEach { holder ->
            val category = RankedWho.configuration.getCategory(holder.category)

            holder.data.forEach { (key, value) ->
                category[key] = Property(key, value.toString(), Property.Type.STRING)
            }
        }

        RankedWho.configuration.save()
    }
}
