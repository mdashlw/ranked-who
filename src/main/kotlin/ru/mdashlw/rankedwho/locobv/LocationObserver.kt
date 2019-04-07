package ru.mdashlw.rankedwho.locobv

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent
import ru.mdashlw.rankedwho.OBJECT_MAPPER
import ru.mdashlw.rankedwho.RankedWho
import ru.mdashlw.rankedwho.reference.HYPIXEL_IP
import ru.mdashlw.rankedwho.reference.LOCRAW_COMMAND
import ru.mdashlw.rankedwho.util.minecraft
import ru.mdashlw.rankedwho.util.thePlayer
import java.net.InetSocketAddress
import java.util.concurrent.Executors

object LocationObserver {
    private val DISPATCHER = Executors.newSingleThreadScheduledExecutor().asCoroutineDispatcher()

    private var waitingForLocation: Boolean = false

    @SubscribeEvent
    fun WorldEvent.Load.onWorldLoad() {
        if (!RankedWho.isOnHypixel) {
            return
        }

        if (waitingForLocation) {
            return
        }

        waitingForLocation = true

        GlobalScope.launch(DISPATCHER) {
            delay(1000)

            while (minecraft.thePlayer == null) {
                delay(300)
            }

            thePlayer.sendChatMessage(LOCRAW_COMMAND)
        }
    }

    @SubscribeEvent
    fun FMLNetworkEvent.ClientConnectedToServerEvent.onClientConnectedToServer() {
        val address = (manager.remoteAddress as? InetSocketAddress)?.hostName ?: return

        RankedWho.isOnHypixel = HYPIXEL_IP in address
    }

    @SubscribeEvent
    fun FMLNetworkEvent.ClientDisconnectionFromServerEvent.onClientDisconnectionFromServer() {
        RankedWho.isOnHypixel = false
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

        val json = OBJECT_MAPPER.readTree(text)

        val gameType = json.get("gametype")?.asText() ?: return
        val mode = json.get("mode")?.asText() ?: return

        RankedWho.isRanked = gameType == "SKYWARS" && mode == "ranked_normal"
    }
}
