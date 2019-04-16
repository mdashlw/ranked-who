package ru.mdashlw.rankedwho.command.impl

import ru.mdashlw.hypixel.HypixelAPI
import ru.mdashlw.rankedwho.command.Command
import ru.mdashlw.rankedwho.scraper.impl.PlayerScraper
import ru.mdashlw.rankedwho.util.send

object RatingCommand : Command() {
    override val aliases: List<String> = listOf("rating")
    override val async: Boolean = true

    override fun execute(args: List<String>) {
        if (args.isEmpty()) {
            "§fUsage: §a/rating <player>".send()
            return
        }

        val player = HypixelAPI.getPlayerByName(args.first())

        if (player == null) {
            "§cPlayer does not exist".send()
            return
        }

        val scraper = PlayerScraper(player.displayname)

        val rating = scraper.rating
        val position = scraper.position

        "§fRating of ${player.formattedDisplayname}§f: §e$rating §b#$position".send()
    }
}
