@file:Suppress("NOTHING_TO_INLINE")

package ru.mdashlw.rankedwho.util

import net.minecraftforge.common.config.Configuration

inline fun Configuration.isEmpty() = categoryNames.isEmpty()
