package com.lagradost.cloudstream3.movieproviders

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor
import org.jsoup.nodes.Element

class DiziDayProvider : MainAPI() {
    override var mainUrl = "https://diziday.de"
    override var name = "DiziDay"
    override var lang = "tr"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(
        TvType.TvSeries
    )

    override val mainPage = mainPageOf(
        "$mainUrl/son-bolumler" to "Son Bölümler",
        "$mainUrl/populer" to "Popüler Diziler",
        "$mainUrl/imdb-7-puan" to "IMDB 7+ Diziler",
        "$mainUrl/turkce-altyazili" to "Türkçe Altyazılı",
        "$mainUrl/turkce-dublaj" to "Türkçe Dublaj"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val document = app.get(request.data + if (page > 1) "/sayfa/$page" else "").document
        val home = document.select("div.dizi-kutusu").map { it.toSearchResult() }
        return newHomePageResponse(request.name, home)
    }

    private fun Element.toSearchResult(): SearchResponse {
        val title = this.selectFirst("div.dizi-adi")?.text()?.trim() ?: ""
        val href = this.selectFirst("a")?.attr("href") ?: ""
        val posterUrl = this.selectFirst("img")?.attr("src")
        val year = this.selectFirst("span.yil")?.text()?.toIntOrNull()
        
        return TvSeriesSearchResponse(
            name = title,
            url = href,
            apiName = this@DiziDayProvider.name,
            type = TvType.TvSeries,
            posterUrl = posterUrl,
            year = year
        )
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val document = app.get("$mainUrl/arama/$query").document
        return document.select("div.dizi-kutusu").map { it.toSearchResult() }
    }

    override suspend fun load(url: String): LoadResponse {
        val document = app.get(url).document

        val title = document.selectFirst("div.dizi-adi")?.text()?.trim() ?: ""
        val poster = document.selectFirst("div.dizi-afis img")?.attr("src")
        val plot = document.selectFirst("div.dizi-aciklama")?.text()?.trim()
        
        val year = document.select("div.dizi-bilgi span").find { it.text().contains("Yapım Yılı") }
            ?.text()?.substringAfter(":")?.trim()?.toIntOrNull()
            
        val tags = document.select("div.dizi-bilgi span.tur a").map { it.text() }

        val episodes = document.select("div.bolumler a").map { episode ->
            Episode(
                data = episode.attr("href"),
                name = episode.text().trim(),
                season = episode.attr("data-sezon")?.toIntOrNull(),
                episode = episode.attr("data-bolum")?.toIntOrNull()
            )
        }

        return newTvSeriesLoadResponse(
            name = title,
            url = url,
            type = TvType.TvSeries,
            episodes = episodes
        ) {
            this.posterUrl = poster
            this.year = year
            this.plot = plot
            this.tags = tags
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val document = app.get(data).document
        
        document.select("div.player iframe").forEach {
            val iframe = it.attr("src")
            loadExtractor(iframe, data, subtitleCallback, callback)
        }
        
        return true
    }
}

