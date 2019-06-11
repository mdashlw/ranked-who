package ru.mdashlw.rankedwho

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import ru.mdashlw.rankedwho.events.PlayerJoinRankedEvent
import ru.mdashlw.rankedwho.events.PlayerJoinWorldEvent
import ru.mdashlw.rankedwho.managers.RankedManager
import ru.mdashlw.rankedwho.reference.GAME_START
import ru.mdashlw.rankedwho.reference.WHO_OUTPUT_MODE
import ru.mdashlw.rankedwho.reference.WHO_OUTPUT_PATTERN
import ru.mdashlw.rankedwho.util.MinecraftUtil
import ru.mdashlw.rankedwho.util.thePlayer
import ru.mdashlw.rankedwho.util.toChatComponent
import ru.mdashlw.util.get

object Listener {
    @SubscribeEvent
    fun ClientChatReceivedEvent.onClientChatReceived() {
        if (!MainConfiguration.toggled) {
            return
        }

        val text = message.unformattedText

        when {
            text == GAME_START -> {
                isCanceled = true

                if (MainConfiguration.autoWho) {
                    thePlayer.sendChatMessage("/who")
                }
            }
            text == WHO_OUTPUT_MODE -> isCanceled = true
            WHO_OUTPUT_PATTERN.matches(text) -> {
                if (!RankedWho.inRanked) {
                    return
                }

                isCanceled = true

                val match = WHO_OUTPUT_PATTERN.find(text)!!

                val team = match[1].toInt()
                val name = match[2]

                GlobalScope.launch(RankedWho.waitPool) {
                    val job = RankedManager.getOrRetrieveAsync(name)
                    val player = job.await()

                    thePlayer.addChatMessage(player.generateMessage(team).toChatComponent())
                }
            }
        }
    }

    @SubscribeEvent
    fun PlayerJoinRankedEvent.onPlayerJoinRanked() {
        if (!MainConfiguration.toggled) {
            return
        }

        RankedManager.clear()

        MinecraftUtil.players.forEach(RankedManager::add)
    }

    @SubscribeEvent
    fun PlayerJoinWorldEvent.onPlayerJoinWorld() {
        if (!MainConfiguration.toggled) {
            return
        }

        if (!RankedWho.inRanked) {
            return
        }

        RankedManager.addIfAbsent(player.name)
    }
}
