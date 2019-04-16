package ru.mdashlw.rankedwho.nickedwinnerreveal

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.minecraft.event.ClickEvent
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import ru.mdashlw.rankedwho.RankedWho
import ru.mdashlw.rankedwho.managers.NickedManager
import ru.mdashlw.rankedwho.managers.RankedManager
import ru.mdashlw.rankedwho.player.impl.NickedPlayer
import ru.mdashlw.rankedwho.reference.GAME_STATS
import ru.mdashlw.rankedwho.reference.WINNER_PATTERN
import ru.mdashlw.rankedwho.scraper.impl.StatsScraper
import ru.mdashlw.rankedwho.util.send
import ru.mdashlw.util.matchresult.get

// TODO Refactor
object NickedWinnerReveal {
    private var current: String? = null

    @SubscribeEvent
    fun ClientChatReceivedEvent.onClientChatReceived() {
        val text = message.unformattedText

        when {
            text == GAME_STATS && current != null -> {
                isCanceled = true

                val clickEvent = message.chatStyle.chatClickEvent

                if (clickEvent.action != ClickEvent.Action.OPEN_URL) {
                    throw IllegalStateException("Expected action open_url for stats link, but got ${clickEvent.action}")
                }

                val url = clickEvent.value

                GlobalScope.launch(RankedWho.singlePool) {
                    val scraper = StatsScraper(url)
                    val winner = scraper.winner

                    NickedManager.put(current!!, winner)

                    "§fReal IGN of §7$current§f: §b$winner".send()

                    current = null
                }
            }
            WINNER_PATTERN.matches(text) -> {
                val match = WINNER_PATTERN.find(text)!!
                val name = match[1]

                val player = RankedManager.get(name) ?: return

                @Suppress("FoldInitializerAndIfToElvis") // No, Kotlin
                if (player !is NickedPlayer) {
                    return
                }

                current = name
            }
        }
    }
}
