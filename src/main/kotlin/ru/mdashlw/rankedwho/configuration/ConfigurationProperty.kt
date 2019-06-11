package ru.mdashlw.rankedwho.configuration

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@Suppress("UNCHECKED_CAST")
class ConfigurationProperty<T : Any>(
    private val value: T,
    private inline val onChange: (T) -> Unit = {}
) : ReadWriteProperty<ConfigurationHolder, T> {
    operator fun provideDelegate(holder: ConfigurationHolder, property: KProperty<*>): ConfigurationProperty<T> {
        holder.data[property.name] = value
        return this
    }

    override fun getValue(thisRef: ConfigurationHolder, property: KProperty<*>): T =
        thisRef.data[property.name] as T

    override fun setValue(thisRef: ConfigurationHolder, property: KProperty<*>, value: T) {
        thisRef.data[property.name] = value
        onChange(value)

        ConfigurationManager.save()
    }
}
