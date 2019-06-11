package ru.mdashlw.rankedwho.command.impl

import ru.mdashlw.hypixel.api.HypixelApi
import ru.mdashlw.rankedwho.MainConfiguration
import ru.mdashlw.rankedwho.command.Command
import ru.mdashlw.rankedwho.util.send
import ru.mdashlw.util.toUuidOrNull

object RankedWhoCommand : Command() {
    override val aliases: List<String> = listOf("rankedwho", "rw")
    override val async: Boolean = true

    override fun execute(args: List<String>) {
        if (args.isEmpty()) {
            "§fUsage: §a/rankedwho toggle | autoWho | <API key>§f.".send()
            return
        }

        val option = args.first()

        when (option.toLowerCase()) {
            "toggle" -> {
                MainConfiguration.toggled = !MainConfiguration.toggled

                if (MainConfiguration.toggled) {
                    "§fRankedWho: §aEnabled§f.".send()
                } else {
                    "§fRankedWho: §cDisabled§f.".send()
                }
            }
            "autowho" -> {
                MainConfiguration.autoWho = !MainConfiguration.autoWho

                if (MainConfiguration.autoWho) {
                    "§b[RankedWho] §fAutoWho: §aEnabled§f.".send()
                } else {
                    "§b[RankedWho] §fAutoWho: §cDisabled§f.".send()
                }
            }
            else -> {
                val apiKey = option.toUuidOrNull()

                if (apiKey == null) {
                    "§cInvalid format.".send()
                    return
                }

                if (HypixelApi.retrieveKey(apiKey.toString()) == null) {
                    "§cKey is invalid.".send()
                    return
                }

                MainConfiguration.apiKey = apiKey.toString()

                "§b[RankedWho] §aHypixel API key has been set.".send()
            }
        }
    }
}
