package ru.mdashlw.rankedwho.command.impl

import ru.mdashlw.hypixel.api.HypixelApi
import ru.mdashlw.hypixel.api.ranked.HypixelRankedApi
import ru.mdashlw.hypixel.api.ranked.exceptions.HypixelRankedApiException
import ru.mdashlw.rankedwho.command.Command
import ru.mdashlw.rankedwho.util.send

object RatingCommand : Command() {
    override val aliases: List<String> = listOf("rating")
    override val async: Boolean = true

    override fun execute(args: List<String>) {
        if (args.isEmpty()) {
            "§fUsage: §a/rating <player>§f.".send()
            return
        }

        val player = HypixelApi.retrievePlayerByName(args.first())

        if (player == null) {
            "§cPlayer does not exist.".send()
            return
        }

        val uuid = player.uuid
        val rankedPlayer = try {
            HypixelRankedApi.retrievePlayer(uuid)
        } catch (exception: HypixelRankedApiException) {
            null
        }

        if (rankedPlayer == null) {
            "§cUnable to reach website.".send()
            return
        }

        val rating = rankedPlayer.rating
        val position = rankedPlayer.position

        "§fRating of ${player.formattedDisplayname}§f: §e$rating §b#$position".send()
    }
}
