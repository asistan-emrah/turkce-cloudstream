package com.asistan.emrah.filmmodu

import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import android.content.Context

@CloudstreamPlugin
class FilmModuPlugin : Plugin() {
    override fun load(context: Context) {
        registerMainAPI(FilmModu())
    }
}
