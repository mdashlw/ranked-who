package ru.mdashlw.rankedwho.reference

const val HYPIXEL_IP = "hypixel.net"
const val LOCRAW_COMMAND = "/locraw"
const val SKYWARS_TYPE = "SKYWARS"
const val RANKED_MODE = "ranked_normal"
const val GAME_START = "Teaming is not allowed on Ranked mode!"
const val WHO_OUTPUT_MODE = "Mode: RANKED"
const val GAME_STATS = "Click to view the stats of your SkyWars§e game!"

@JvmField
val WHO_OUTPUT_PATTERN = "Team #(\\d+): (.+)".toRegex()

@JvmField
val WINNER_PATTERN = " {24}Winner - (?:\\[[\\w+]+] )?(.+)".toRegex()
