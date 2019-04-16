package ru.mdashlw.rankedwho.command.impl

import ru.mdashlw.rankedwho.command.Command
import ru.mdashlw.rankedwho.configuration.holder.impl.MainConfiguration
import ru.mdashlw.rankedwho.util.send
import ru.mdashlw.util.string.toUuidOrNull

object RankedWhoCommand : Command() {
    override val aliases: List<String> = listOf("rankedwho", "rw")

    override fun execute(args: List<String>) {
        if (args.isEmpty()) {
            "§fUsage: §a/rankedwho <API key>".send()
            return
        }

        val apiKey = args.first().toUuidOrNull()

        if (apiKey == null) {
            "§cInvalid API key format".send()
            return
        }

        // TODO Send a request to Hypixel API to check if the key is valid

        MainConfiguration.apiKey = apiKey

        "§b[RankedWho] §aHypixel API key has been installed".send()
    }
}
