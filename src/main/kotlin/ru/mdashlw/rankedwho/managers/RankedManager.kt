package ru.mdashlw.rankedwho.managers

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import ru.mdashlw.hypixel.HypixelAPI
import ru.mdashlw.rankedwho.RankedWho
import ru.mdashlw.rankedwho.player.Player
import ru.mdashlw.rankedwho.player.impl.NickedPlayer
import ru.mdashlw.rankedwho.player.impl.RegularPlayer
import java.util.concurrent.ConcurrentHashMap

object RankedManager {
    private val jobs: ConcurrentHashMap<String, Deferred<Player>> = ConcurrentHashMap()
    private val players: ConcurrentHashMap<String, Player> = ConcurrentHashMap()

    private fun retrieveInformation(name: String): Player {
        val player = HypixelAPI.getPlayerByName(name)

        return if (player == null) {
            NickedPlayer.create(name)
        } else {
            RegularPlayer.create(player)
        }
    }

    private fun retrieveInformationAsync(name: String): Deferred<Player> =
        GlobalScope.async(RankedWho.pool) {
            retrieveInformation(name)
                .also {
                    players[name] = it
                }
        }

    fun get(player: String): Player? = players[player]

    fun getOrRetrieveAsync(player: String): Deferred<Player> = jobs[player] ?: retrieveInformationAsync(player)

    fun add(player: String) {
        jobs[player] = retrieveInformationAsync(player)
    }

    // Don't use putIfAbsent
    fun addIfAbsent(player: String) {
        if (jobs.containsKey(player)) {
            return
        }

        add(player)
    }

    fun clear() {
        jobs.clear()
    }
}
