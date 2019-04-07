package ru.mdashlw.rankedwho.events

import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fml.common.eventhandler.Event

class PlayerJoinWorldEvent(val player: EntityPlayer) : Event()
