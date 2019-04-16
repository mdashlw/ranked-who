package ru.mdashlw.rankedwho.scraper

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

abstract class WebScraper(url: String) {
    val html: Document = Jsoup.connect(url)
        .apply {
            userAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:66.0) Gecko/20100101 Firefox/66.0")
            ignoreContentType(true)
            ignoreHttpErrors(true)
            followRedirects(true)
        }
        .get()
}
