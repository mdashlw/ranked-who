package ru.mdashlw.rankedwho.eventdispatcher

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
