package ru.mdashlw.rankedwho.player

interface Player {
    val displayname: String
    val rating: Int
    val level: Int
    val kit: String

    fun generateMessage(team: Int): String
}
