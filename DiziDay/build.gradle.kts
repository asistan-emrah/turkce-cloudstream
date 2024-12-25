version = 2

cloudstream {
    authors     = listOf("asistan-emrah")
    language    = "tr"
    description = "Güncel ve en iyi yabancı filmleri yüksek görüntü kalitesinde, Türkçe dublaj ve altyazı seçenekleriyle filmleri full izleyin."

    /**
     * Status int as the following:
     * 0: Down
     * 1: Ok
     * 2: Slow
     * 3: Beta only
    **/
    status  = 1 // will be 3 if unspecified
    tvTypes = listOf("Series")
    iconUrl = "https://www.google.com/s2/favicons?domain=www.sinema.cx&sz=%size%"
}
