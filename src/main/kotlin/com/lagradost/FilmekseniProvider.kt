package com.lagradost.filmekseni

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor
import org.jsoup.nodes.Element

class FilmekseniProvider : MainAPI() {
    override var mainUrl = "https://filmekseni.net"
    override var name = "Filmekseni"
    override val hasMainPage = true
    override var lang = "tr"
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(
        TvType.Movie,
        TvType.TvSeries
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val document = app.get(mainUrl).document
        val homePageList = ArrayList<HomePageList>()

        // Son Eklenen Filmler
        val recentMovies = document.select("div.son-eklenen-filmler div.film-kutusu").mapNotNull { it.toSearchResult() }
        if (recentMovies.isNotEmpty()) homePageList.add(HomePageList("Son Eklenen Filmler", recentMovies))

        // En Çok İzlenen Filmler
        val popularMovies = document.select("div.en-cok-izlenen-filmler div.film-kutusu").mapNotNull { it.toSearchResult() }
        if (popularMovies.isNotEmpty()) homePageList.add(HomePageList("En Çok İzlenen Filmler", popularMovies))

        return HomePageResponse(homePageList)
    }

    private fun Element.toSearchResult(): SearchResponse? {
        val title = this.selectFirst("div.film-adi")?.text()?.trim() ?: return null
        val href = fixUrl(this.selectFirst("a")?.attr("href") ?: return null)
        val posterUrl = fixUrlNull(this.selectFirst("img")?.attr("src"))
        val quality = this.selectFirst("div.kalite")?.text()?.trim()

        return MovieSearchResponse(
            name = title,
            url = href,
            apiName = this@FilmekseniProvider.name,
            type = TvType.Movie,
            posterUrl = posterUrl,
            quality = getQualityFromString(quality)
        )
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$mainUrl/?s=$query"
        val document = app.get(url).document

        return document.select("div.film-kutusu").mapNotNull { it.toSearchResult() }
    }

    override suspend fun load(url: String): LoadResponse? {
        val document = app.get(url).document

        val title = document.selectFirst("div.film-bilgileri h1")?.text()?.trim() ?: return null
        val poster = fixUrlNull(document.selectFirst("div.film-afis img")?.attr("src"))
        val tags = document.select("div.tur a").map { it.text().trim() }
        val year = document.selectFirst("div.yapim-yili")?.text()?.trim()?.toIntOrNull()
        val description = document.selectFirst("div.film-ozet")?.text()?.trim()
        val rating = document.selectFirst("div.imdb-puan")?.text()?.trim()?.toRatingInt()

        val recommendations = document.select("div.benzer-filmler div.film-kutusu").mapNotNull { it.toSearchResult() }

        // Tek parça linklerini topla
        val episodeLinks = document.select("div.tek-part a").mapNotNull { element ->
            val name = element.text().trim()
            val link = element.attr("href")
            if (link.isNotBlank()) {
                Episode(
                    data = link,
                    name = name
                )
            } else null
        }

        return MovieLoadResponse(
            name = title,
            url = url,
            apiName = this.name,
            type = TvType.Movie,
            dataUrl = url,
            posterUrl = poster,
            year = year,
            plot = description,
            tags = tags,
            rating = rating,
            recommendations = recommendations,
            episodes = episodeLinks
        )
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val document = app.get(data).document
        val iframe = document.selectFirst("div.player-container iframe")?.attr("src") ?: return false
        loadExtractor(iframe, data, subtitleCallback, callback)
        return true
    }
}

