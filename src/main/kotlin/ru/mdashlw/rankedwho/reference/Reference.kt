package ru.mdashlw.rankedwho.reference

import java.util.*

@JvmField
val API_KEY: UUID = UUID.fromString("2e8b48fb-629f-48b1-941c-f110079b904a")

const val HYPIXEL_IP = "hypixel.net"
const val LOCRAW_COMMAND = "/locraw"

const val GAME_START_PATTERN = "Teaming is not allowed on Ranked mode!"
const val WHO_OUTPUT_FIRST_LINE_PATTERN = "Mode: RANKED"
@JvmField
val WHO_OUTPUT_PATTERN = "Team #\\d+: (.+)".toRegex()
@JvmField
val RATING_POSITION_PATTERN =
    "<tr class=\"row2\">\n<td class=\"statName\">Ranked Score</td>\n<td class=\"statValue\">\n(.+)\n</td>\n</tr>\n<tr class=\"row1\">\n<td class=\"statName\">Rank</td>\n<td class=\"statValue\">\n(.+)\n</td>\n</tr>".toRegex()
