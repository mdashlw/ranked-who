package ru.mdashlw.rankedwho.player.impl

import ru.mdashlw.rankedwho.managers.NickedManager
import ru.mdashlw.rankedwho.player.Player

class NickedPlayer(override val displayname: String) : Player {
    override val rating: Int = -1
    override val kit: String = ""
    override val level: Int = 0

    override fun generateMessage(team: Int): String =
        "§fTeam #$team: §7$displayname §cNICKED"

    companion object {
        fun create(nick: String): Player {
            val name = NickedManager.get(nick)
                ?: return NickedPlayer(nick)

            return RegularPlayer.createNicked(name, nick)
        }
    }
}
