package com.diziday

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor
import org.jsoup.nodes.Element

class DizidayProvider : MainAPI() {
    override var mainUrl = "https://diziday.de"
    override var name = "Diziday"
    override var lang = "tr"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(
        TvType.Movie,
        TvType.TvSeries,
        TvType.Anime,
        TvType.Documentary
    )

    override val mainPage = mainPageOf(
        "$mainUrl/dizi-izle/page/" to "Diziler",
        "$mainUrl/film-izle/page/" to "Filmler",
        "$mainUrl/anime-izle/page/" to "Animeler",
        "$mainUrl/belgesel-izle/page/" to "Belgeseller"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val document = app.get(request.data + page).document
        val items = document.select("div.movie-box").map {
            it.toSearchResult()
        }
        return newHomePageResponse(request.name, items)
    }

    private fun Element.toSearchResult(): SearchResponse {
        val title = this.selectFirst("h2.movie-title")?.text()?.trim() ?: ""
        val href = this.selectFirst("a")?.attr("href") ?: ""
        val posterUrl = this.selectFirst("img")?.attr("src")
        val type = when {
            href.contains("/dizi/") -> TvType.TvSeries
            href.contains("/anime/") -> TvType.Anime
            href.contains("/belgesel/") -> TvType.Documentary
            else -> TvType.Movie
        }
        
        return if (type == TvType.Movie) {
            newMovieSearchResponse(title, href, TvType.Movie) {
                this.posterUrl = posterUrl
            }
        } else {
            newTvSeriesSearchResponse(title, href, type) {
                this.posterUrl = posterUrl
            }
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val document = app.get("$mainUrl/?s=$query").document
        return document.select("div.movie-box").map { it.toSearchResult() }
    }

    override suspend fun load(url: String): LoadResponse {
        val document = app.get(url).document

        val title = document.selectFirst("h1.movie-title")?.text()?.trim() ?: ""
        val poster = document.selectFirst("div.movie-poster img")?.attr("src")
        val plot = document.selectFirst("div.movie-description")?.text()?.trim()
        val tags = document.select("div.movie-info span.genre").map { it.text() }
        val year = document.selectFirst("div.movie-info span.year")?.text()?.toIntOrNull()
        val rating = document.selectFirst("div.movie-info span.imdb")?.text()?.toDoubleOrNull()

        val type = when {
            url.contains("/dizi/") -> TvType.TvSeries
            url.contains("/anime/") -> TvType.Anime
            url.contains("/belgesel/") -> TvType.Documentary
            else -> TvType.Movie
        }

        return if (type == TvType.Movie) {
            newMovieLoadResponse(title, url, type, url) {
                this.posterUrl = poster
                this.plot = plot
                this.tags = tags
                this.year = year
                this.rating = rating
            }
        } else {
            val episodes = document.select("div.episodes-list div.episode").map { ep ->
                val epTitle = ep.selectFirst("span.episode-title")?.text() ?: ""
                val epHref = ep.selectFirst("a")?.attr("href") ?: ""
                Episode(
                    epHref,
                    epTitle
                )
            }
            newTvSeriesLoadResponse(title, url, type, episodes) {
                this.posterUrl = poster
                this.plot = plot
                this.tags = tags
                this.year = year
                this.rating = rating
            }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val document = app.get(data).document
        val sources = document.select("div.player-box iframe").map { it.attr("src") }
        
        sources.forEach { source ->
            loadExtractor(source, data, subtitleCallback, callback)
        }
        
        return true
    }
}

