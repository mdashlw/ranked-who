package ru.mdashlw.rankedwho.managers

import java.util.concurrent.ConcurrentHashMap

object NickedManager {
    private val names: ConcurrentHashMap<String, String> = ConcurrentHashMap()

    fun get(name: String): String? = names[name]

    fun put(nicked: String, real: String) {
        names[nicked] = real
    }
}
