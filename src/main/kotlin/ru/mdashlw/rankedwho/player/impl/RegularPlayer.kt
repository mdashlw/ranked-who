package ru.mdashlw.rankedwho.player.impl

import ru.mdashlw.hypixel.api.HypixelApi
import ru.mdashlw.hypixel.api.entities.player.HypixelPlayer
import ru.mdashlw.hypixel.api.enums.RankedKit
import ru.mdashlw.hypixel.api.enums.SkyWarsType
import ru.mdashlw.hypixel.api.ranked.HypixelRankedApi
import ru.mdashlw.hypixel.api.ranked.exceptions.HypixelRankedApiException
import ru.mdashlw.rankedwho.player.Player
import ru.mdashlw.util.format

class RegularPlayer(
    override val displayname: String,
    override val rating: Int,
    override val level: Int,
    override val kit: String,
    private val nick: String? = null
) : Player {
    override fun generateMessage(team: Int): String =
        "§fTeam #$team: §7$displayname §e${rating.format()} §f[§a$level§f] §f(§c$kit§f)" +
                (nick?.let {
                    " §d(nicked as §7$nick§d)"
                } ?: "")

    companion object {
        fun create(player: HypixelPlayer, nick: String? = null): RegularPlayer {
            val uuid = player.uuid
            val displayname = player.formattedDisplayname
            val rankedPlayer = try {
                HypixelRankedApi.retrievePlayer(uuid)
            } catch (exception: HypixelRankedApiException) {
                null
            }
            val rating = rankedPlayer?.rating ?: -1
            val level = player.level.toInt()
            val game = player.stats?.SkyWars
            val activeKit = game?.getActiveKit<RankedKit>(SkyWarsType.RANKED)?.localizedName ?: "None"

            return RegularPlayer(
                displayname,
                rating,
                level,
                activeKit,
                nick
            )
        }

        fun createNicked(name: String, nick: String): RegularPlayer =
            create(HypixelApi.retrievePlayerByName(name)!!, nick)
    }
}
