package ru.mdashlw.rankedwho.updater

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent
import ru.mdashlw.rankedwho.util.minecraft
import ru.mdashlw.rankedwho.util.thePlayer

object Listener {
    @SubscribeEvent
    fun FMLNetworkEvent.ClientConnectedToServerEvent.onClientConnectedToServer() {
        GlobalScope.launch {
            while (minecraft.thePlayer == null) {
                delay(300)
            }

            thePlayer.addChatMessage(Updater.message)
        }
    }
}
