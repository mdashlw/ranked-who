package ru.mdashlw.rankedwho.util

import net.minecraft.client.Minecraft

inline val minecraft
    get() = Minecraft.getMinecraft()

inline val thePlayer
    get() = minecraft.thePlayer
