package ru.mdashlw.rankedwho.locationobserver

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent
import ru.mdashlw.rankedwho.RankedWho
import ru.mdashlw.rankedwho.reference.HYPIXEL_IP
import ru.mdashlw.rankedwho.reference.LOCRAW_COMMAND
import ru.mdashlw.rankedwho.reference.RANKED_MODE
import ru.mdashlw.rankedwho.reference.SKYWARS_TYPE
import ru.mdashlw.rankedwho.util.thePlayer
import java.net.InetSocketAddress
import java.util.concurrent.Executors

object LocationObserver {
    private val dispatcher = Executors.newSingleThreadScheduledExecutor().asCoroutineDispatcher()

    private var waitingForLocation: Boolean = false

    @SubscribeEvent
    fun WorldEvent.Load.onWorldLoad() {
        if (!RankedWho.onHypixel) {
            return
        }

        if (waitingForLocation) {
            return
        }

        waitingForLocation = true

        GlobalScope.launch(dispatcher) {
            delay(1000)

            while (thePlayer == null) {
                delay(300)
            }

            thePlayer.sendChatMessage(LOCRAW_COMMAND)
        }
    }

    @SubscribeEvent
    fun FMLNetworkEvent.ClientConnectedToServerEvent.onClientConnectedToServer() {
        val address = (manager.remoteAddress as? InetSocketAddress)?.hostName ?: return

        RankedWho.onHypixel = HYPIXEL_IP in address
    }

    @SubscribeEvent
    fun FMLNetworkEvent.ClientDisconnectionFromServerEvent.onClientDisconnectionFromServer() {
        RankedWho.onHypixel = false
    }

    @SubscribeEvent
    fun ClientChatReceivedEvent.onClientChatReceived() {
        val text = message.unformattedText

        if (!waitingForLocation) {
            return
        }

        if (!text.startsWith("{") || !text.endsWith("}")) {
            return
        }

        isCanceled = true
        waitingForLocation = false

        val json = RankedWho.jackson.readTree(text)

        val gameType = json.get("gametype")?.asText() ?: return
        val mode = json.get("mode")?.asText() ?: return

        RankedWho.inRanked = gameType == SKYWARS_TYPE && mode == RANKED_MODE
    }
}
