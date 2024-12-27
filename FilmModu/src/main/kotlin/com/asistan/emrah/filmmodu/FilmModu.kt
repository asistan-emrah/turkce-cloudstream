package com.asistan.emrah.filmmodu

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor
import org.jsoup.Jsoup

class FilmModu : MainAPI() {
    override var mainUrl = "https://www.filmmodu.org"
    override var name = "FilmModu"
    override val hasMainPage = true
    override var lang = "tr"
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(
        TvType.Movie,
        TvType.TvSeries
    )

    override val mainPage = mainPageOf(
        "$mainUrl/filmler/page/" to "Filmler",
        "$mainUrl/diziler/page/" to "Diziler",
        "$mainUrl/animeler/page/" to "Animeler"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val url = request.data + page
        val document = app.get(url).document
        val items = document.select("div.movie-box").map { element ->
            val title = element.selectFirst("h2")?.text() ?: ""
            val poster = element.selectFirst("img")?.attr("src")
            val href = fixUrl(element.selectFirst("a")?.attr("href") ?: "")
            val year = element.selectFirst("span.year")?.text()?.toIntOrNull()
            val quality = element.selectFirst("span.quality")?.text()

            newMovieSearchResponse(
                name = title,
                url = href,
                type = if (href.contains("/dizi/")) TvType.TvSeries else TvType.Movie
            ) {
                this.posterUrl = poster
                this.year = year
                this.quality = getQualityFromString(quality)
            }
        }
        return newHomePageResponse(request.name, items)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$mainUrl/arama/$query"
        val document = app.get(url).document
        return document.select("div.movie-box").map { element ->
            val title = element.selectFirst("h2")?.text() ?: ""
            val href = fixUrl(element.selectFirst("a")?.attr("href") ?: "")
            val posterUrl = element.selectFirst("img")?.attr("src")
            val year = element.selectFirst("span.year")?.text()?.toIntOrNull()
            val quality = element.selectFirst("span.quality")?.text()

            if (href.contains("/dizi/")) {
                newTvSeriesSearchResponse(title, href, TvType.TvSeries) {
                    this.posterUrl = posterUrl
                    this.year = year
                    this.quality = getQualityFromString(quality)
                }
            } else {
                newMovieSearchResponse(title, href, TvType.Movie) {
                    this.posterUrl = posterUrl
                    this.year = year
                    this.quality = getQualityFromString(quality)
                }
            }
        }
    }

    override suspend fun load(url: String): LoadResponse {
        val document = app.get(url).document
        val title = document.selectFirst("h1.movie-title")?.text() ?: ""
        val poster = document.selectFirst("div.movie-poster img")?.attr("src")
        val year = document.selectFirst("span.year")?.text()?.toIntOrNull()
        val plot = document.selectFirst("div.story p")?.text()
        val rating = document.selectFirst("span.imdb")?.text()?.toRatingInt()
        val tags = document.select("div.genres a").map { it.text() }

        val actors = document.select("div.cast-list div.cast-box").map { actorElement ->
            Actor(
                actorElement.selectFirst("h2")?.text() ?: "",
                actorElement.selectFirst("img")?.attr("src")
            )
        }

        return if (url.contains("/dizi/")) {
            val episodes = document.select("div.episode-box").map { episodeElement ->
                val episodeTitle = episodeElement.selectFirst("h2")?.text() ?: ""
                val episodeNumber = episodeTitle.substringAfter("Bölüm ").toIntOrNull()
                val seasonNumber = episodeTitle.substringAfter("Sezon ").substringBefore(" Bölüm").toIntOrNull()
                val episodeUrl = fixUrl(episodeElement.selectFirst("a")?.attr("href") ?: "")

                Episode(
                    data = episodeUrl,
                    name = episodeTitle,
                    season = seasonNumber,
                    episode = episodeNumber
                )
            }
            newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
                this.posterUrl = poster
                this.year = year
                this.plot = plot
                this.rating = rating
                this.tags = tags
                this.actors = actors
            }
        } else {
            newMovieLoadResponse(title, url, TvType.Movie, url) {
                this.posterUrl = poster
                this.year = year
                this.plot = plot
                this.rating = rating
                this.tags = tags
                this.actors = actors
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
        document.select("div.video-player iframe").forEach { iframe ->
            val src = iframe.attr("src")
            loadExtractor(src, data, subtitleCallback, callback)
        }
        return true
    }
}
