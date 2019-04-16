package ru.mdashlw.rankedwho.player.impl

import ru.mdashlw.hypixel.HypixelAPI
import ru.mdashlw.hypixel.elements.player.HypixelPlayer
import ru.mdashlw.rankedwho.player.Player
import ru.mdashlw.rankedwho.scraper.impl.PlayerScraper

class RegularPlayer(
    override val displayname: String,
    override val rating: String,
    override val level: Int,
    override val kit: String,
    private val nick: String? = null
) : Player {
    override fun generateMessage(team: Int): String =
        "§fTeam #$team: §7$displayname §e$rating §f[§a$level§f] §f(§c$kit§f)" +
                (nick?.let {
                    " §d(nicked as §7$nick§d)"
                } ?: "")

    companion object {
        fun create(player: HypixelPlayer, nick: String? = null): RegularPlayer {
            val name = player.displayname
            val displayname = player.formattedDisplayname
            val scraper = PlayerScraper(name)
            val rating = scraper.rating
            val level = player.level.toInt()
            val game = player.stats.SkyWars
            val kit = game.activeRankedKit.localizedName

            return RegularPlayer(
                displayname,
                rating,
                level,
                kit,
                nick
            )
        }

        fun createNicked(name: String, nick: String): RegularPlayer =
            create(HypixelAPI.getPlayerByName(name)!!, nick)
    }
}
