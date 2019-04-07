package ru.mdashlw.rankedwho.evdis

import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object EventDispatcher {
    @SubscribeEvent
    fun WorldEvent.Load.onWorldLoad() {
        if (!world.isRemote) {
            return
        }

        world.addWorldAccess(CustomWorldAccess())
    }
}
