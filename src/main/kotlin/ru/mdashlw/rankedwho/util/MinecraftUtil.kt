@file:Suppress("HasPlatformType")

// We need it

package ru.mdashlw.rankedwho.util

import net.minecraft.client.Minecraft

inline val minecraft
    get() = Minecraft.getMinecraft()

inline val thePlayer
    get() = minecraft.thePlayer

object MinecraftUtil {
    val players: Set<String>
        get() = thePlayer.sendQueue.playerInfoMap
            .map { it.gameProfile.name }
            .filter { "§" !in it }
            .toSet() // Set is faster for contains calls
}
