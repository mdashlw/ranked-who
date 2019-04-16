@file:Suppress("NOTHING_TO_INLINE")

package ru.mdashlw.rankedwho.util

import net.minecraft.util.IChatComponent

inline fun IChatComponent.send() {
    thePlayer.addChatMessage(this)
}
