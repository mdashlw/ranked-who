package ru.mdashlw.rankedwho.util

import net.minecraft.entity.player.EntityPlayer

inline val EntityPlayer.isBot: Boolean
    get() = name !in MinecraftUtil.players
