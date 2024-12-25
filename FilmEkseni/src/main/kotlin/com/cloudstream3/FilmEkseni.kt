package com.lagradost.cloudstream3.movieproviders

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor
import org.jsoup.nodes.Element

class FilmEkseniProvider : MainAPI() {
    override var mainUrl = "https://filmekseni.net"
    override var name = "FilmEkseni"
    override var lang = "tr"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(
        TvType.Movie,
        TvType.TvSeries
    )

    override val mainPage = mainPageOf(
        "$mainUrl/son-eklenen-filmler" to "Son Eklenen Filmler",
        "$mainUrl/en-cok-izlenenler" to "En Çok İzlenenler",
        "$mainUrl/imdb-250" to "IMDB 250",
        "$mainUrl/tavsiye-filmler" to "Tavsiye Filmler",
        "$mainUrl/turkce-altyazili" to "Türkçe Altyazılı",
        "$mainUrl/turkce-dublaj" to "Türkçe Dublaj"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val document = app.get(request.data + if (page > 1) "/page/$page" else "").document
        val home = document.select("div.film-kutusu").map { it.toSearchResult() }
        return newHomePageResponse(request.name, home)
    }

    private fun Element.toSearchResult(): SearchResponse {
        val title = this.selectFirst("div.film-adi")?.text()?.trim() ?: ""
        val href = this.selectFirst("a")?.attr("href") ?: ""
        val posterUrl = this.selectFirst("img")?.attr("src")
        val year = this.selectFirst("span.yil")?.text()?.toIntOrNull()
        val quality = this.selectFirst("span.kalite")?.text()
        
        return MovieSearchResponse(
            name = title,
            url = href,
            apiName = this@FilmEkseniProvider.name,
            type = TvType.Movie,
            posterUrl = posterUrl,
            year = year,
            quality = getQualityFromString(quality)
        )
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val document = app.get("$mainUrl/arama/$query").document
        return document.select("div.film-kutusu").map { it.toSearchResult() }
    }

    override suspend fun load(url: String): LoadResponse {
        val document = app.get(url).document

        val title = document.selectFirst("div.film-adi")?.text()?.trim() ?: ""
        val poster = document.selectFirst("div.film-afis img")?.attr("src")
        val plot = document.selectFirst("div.film-aciklama")?.text()?.trim()
        
        val year = document.select("div.film-bilgi span").find { it.text().contains("Yapım Yılı") }
            ?.text()?.substringAfter(":")?.trim()?.toIntOrNull()
            
        val duration = document.select("div.film-bilgi span").find { it.text().contains("Süre") }
            ?.text()?.substringAfter(":")?.trim()?.substringBefore(" ")?.toIntOrNull()
            
        val tags = document.select("div.film-bilgi span.tur a").map { it.text() }
        
        val recommendations = document.select("div.benzer-filmler div.film-kutusu").map { it.toSearchResult() }

        return newMovieLoadResponse(
            name = title,
            url = url,
            type = TvType.Movie,
            dataUrl = url
        ) {
            this.posterUrl = poster
            this.year = year
            this.plot = plot
            this.duration = duration
            this.tags = tags
            this.recommendations = recommendations
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

