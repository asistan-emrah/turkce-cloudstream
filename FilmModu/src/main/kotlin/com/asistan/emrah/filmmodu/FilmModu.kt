package com.asistan.emrah.filmmodu

import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import android.content.Context

@CloudstreamPlugin
class FilmModuPlugin: Plugin() {
    override fun load(context: Context) {
        // Tüm kaynakları burada yükleyin
        registerMainAPI(FilmModu())
    }
}

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

    // Diğer gerekli metodları buraya ekleyin (search, load, loadLinks vb.)
}
