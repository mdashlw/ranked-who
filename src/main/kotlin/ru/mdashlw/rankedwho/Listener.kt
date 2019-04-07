package ru.mdashlw.rankedwho

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.minecraft.util.ChatComponentText
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import ru.mdashlw.rankedwho.events.PlayerJoinRankedEvent
import ru.mdashlw.rankedwho.events.PlayerJoinWorldEvent
import ru.mdashlw.rankedwho.reference.GAME_START_PATTERN
import ru.mdashlw.rankedwho.reference.WHO_OUTPUT_FIRST_LINE_PATTERN
import ru.mdashlw.rankedwho.reference.WHO_OUTPUT_PATTERN
import ru.mdashlw.rankedwho.util.MinecraftUtil
import ru.mdashlw.rankedwho.util.thePlayer
import ru.mdashlw.util.matchresult.get

object Listener {
    @SubscribeEvent
    fun ClientChatReceivedEvent.onClientChatReceived() {
        val text = message.unformattedText

        when {
            text == GAME_START_PATTERN -> {
                isCanceled = true
                thePlayer.sendChatMessage("/who")
            }
            text == WHO_OUTPUT_FIRST_LINE_PATTERN -> {
                isCanceled = true
            }
            WHO_OUTPUT_PATTERN.matches(text) -> {
                if (!RankedWho.isRanked) {
                    return
                }

                val match = WHO_OUTPUT_PATTERN.find(text) ?: throw IllegalStateException()

                isCanceled = true

                val name = match[1]

                GlobalScope.launch(RankedWho.WAITING_POOL) {
                    val job = RankedWho.jobs[name] ?: RankedWho.retrieveInformationAsync(name)
                    val message = job.await()

                    thePlayer.addChatMessage(
                        ChatComponentText(
                            message.replace("{team}", (RankedWho.team++).toString())
                        )
                    )
                }
            }
        }
    }

    @SubscribeEvent
    fun PlayerJoinRankedEvent.onPlayerJoinRanked() {
        RankedWho.jobs.clear()
        RankedWho.team = 1

        MinecraftUtil.players.forEach(RankedWho.Companion::add)
    }

    @SubscribeEvent
    fun PlayerJoinWorldEvent.onPlayerJoinWorld() {
        if (!RankedWho.isRanked) {
            return
        }

        val name = player.name

        if (name in RankedWho.jobs) {
            return
        }

        RankedWho.add(name)
    }
}
