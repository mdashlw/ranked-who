package ru.mdashlw.rankedwho.scraper.impl

import ru.mdashlw.rankedwho.scraper.WebScraper

class StatsScraper(url: String) : WebScraper(url) {
    val winner: String
        get() = html.selectFirst("div.column:nth-child(1) > h3:nth-child(19)").text().substring(8)
}
