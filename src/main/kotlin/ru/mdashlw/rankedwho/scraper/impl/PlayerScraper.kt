package ru.mdashlw.rankedwho.scraper.impl

import ru.mdashlw.rankedwho.scraper.WebScraper

class PlayerScraper(name: String) : WebScraper("https://hypixel.net/player/$name") {
    val rating: String
        get() = html.selectFirst("#stats-content-skywars tr.row2:nth-child(14) .statValue").text()

    val position: String
        get() = html.selectFirst("#stats-content-skywars tr.row1:nth-child(15) .statValue").text()
}
