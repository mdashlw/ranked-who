package ru.mdashlw.rankedwho.util

import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP

inline val minecraft: Minecraft
    get() = Minecraft.getMinecraft()

inline val thePlayer: EntityPlayerSP
    get() = minecraft.thePlayer

class MinecraftUtil private constructor() {
    companion object {
        @JvmStatic
        val players: List<String>
            get() = thePlayer.sendQueue.playerInfoMap
                .map { it.gameProfile.name }
                .filter { !it.startsWith("§") }
    }
}
