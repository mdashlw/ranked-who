package ru.mdashlw.rankedwho.keychecker

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent
import ru.mdashlw.hypixel.api.HypixelApi
import ru.mdashlw.rankedwho.util.send
import ru.mdashlw.rankedwho.util.thePlayer
import ru.mdashlw.util.EMPTY_UUID

object HypixelKeyChecker {
    @SubscribeEvent
    fun FMLNetworkEvent.ClientConnectedToServerEvent.onClientConnectedToServer() {
        if (HypixelApi.keys.isNotEmpty() || HypixelApi.keys.getOrNull(0) == EMPTY_UUID.toString()) {
            return
        }

        GlobalScope.launch {
            while (thePlayer == null) {
                delay(300)
            }

            """
                §c§m----------------------------------------------
                §b[RankedWho] §fHypixel API key is not set.

                Set it up via §a/rankedwho <your API key>
                Use §c/api new §fto get your key.
                §c§m----------------------------------------------
            """.trimIndent().send()
        }
    }
}
