package ru.mdashlw.rankedwho.keychecker

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent
import ru.mdashlw.hypixel.HypixelAPI
import ru.mdashlw.rankedwho.util.send
import ru.mdashlw.rankedwho.util.thePlayer
import ru.mdashlw.util.uuid.isEmpty

object HypixelKeyChecker {
    @SubscribeEvent
    fun FMLNetworkEvent.ClientConnectedToServerEvent.onClientConnectedToServer() {
        if (!HypixelAPI.apiKey.isEmpty()) {
            return
        }

        GlobalScope.launch {
            while (thePlayer == null) {
                delay(300)
            }

            """
                §c§m----------------------------------------------
                §b[RankedWho] §fHypixel API Key is not installed.

                Set it up via §a/rankedwho <your API key>
                Use §c/api new §fto get your own Key.
                §c§m----------------------------------------------
            """.trimIndent().send()
        }
    }
}
