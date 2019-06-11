package ru.mdashlw.rankedwho.util

object MinecraftUtil {
    val players: Set<String>
        get() = thePlayer.sendQueue.playerInfoMap
            .map { it.gameProfile.name }
            .filter { "§" !in it }
            .toSet() // Set is faster for contains calls
}
