package com.lagradost.cloudstream3.extractors

import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.*

class Canlidizi6Extractor : ExtractorApi() {
    override var name = "Canlı Dizi 6"
    override var mainUrl = "https://www.canlidizi6.org"
    override var lang = "tr"
    override val hasMainPage = true
    override val hasQuickSearch = true
    override val hasMovieSearch = true
    override val hasTvSeries = true

    override suspend fun getMainPage(page: Int): HomePageResponse {
        val document = app.get(mainUrl).document
        val items = document.select(".diziler a").map {
            val title = it.text()
            val href = fixUrl(it.attr("href"))
            val posterUrl = fixUrlNull(it.select("img").attr("src"))
            TvSeriesSearchResponse(
                title,
                href,
                this.name,
                TvType.TvSeries,
                posterUrl,
                null
            )
        }
        return newHomePageResponse(items)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val document = app.get("$mainUrl/?ara=$query").document
        return document.select(".diziler a").map {
            val title = it.text()
            val href = fixUrl(it.attr("href"))
            val posterUrl = fixUrlNull(it.select("img").attr("src"))
            TvSeriesSearchResponse(
                title,
                href,
                this.name,
                TvType.TvSeries,
                posterUrl,
                null
            )
        }
    }

    override suspend fun load(url: String): LoadResponse {
        val document = app.get(url).document
        val title = document.select("h1").text()
        val posterUrl = fixUrlNull(document.select(".afis img").attr("src"))
        val episodes = document.select(".bolumler a").map {
            val episodeTitle = it.text()
            val episodeUrl = fixUrl(it.attr("href"))
            Episode(episodeUrl, episodeTitle)
        }
        val tvType = if (episodes.size > 1) TvType.TvSeries else TvType.Movie
        return newTvSeriesLoadResponse(title, url, tvType, episodes) {
            this.posterUrl = posterUrl
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val document = app.get(data).document
        document.select("iframe").forEach {
            val iframeUrl = fixUrl(it.attr("src"))
            loadExtractor(iframeUrl, data, subtitleCallback, callback)
        }
        return true
    }
}
