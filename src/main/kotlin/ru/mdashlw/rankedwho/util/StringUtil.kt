@file:Suppress("NOTHING_TO_INLINE")

package ru.mdashlw.rankedwho.util

import net.minecraft.util.ChatComponentText

inline fun String.toChatComponent(): ChatComponentText = ChatComponentText(this)
