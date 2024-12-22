// use an integer for version numbers
version = 1


android {
    compileSdkVersion(33)
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(33)
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("com.lagradost:cloudstream3:pre-release")
}

cloudstream {
    language = "tr"
    // All of these properties are optional, you can safely remove them

    description = "Türkçe Film ve Dizi İzleme Eklentisi"
    authors = listOf("asistan-emrah")

    /**
     * Status int as the following:
     * 0: Down
     * 1: Ok
     * 2: Slow
     * 3: Beta only
     * */
    status = 1 // will be 1 if unspecified

    // List of video source types. Users can filter for extensions in a given category.
    // You can find a list of avaliable types here: https://recloudstream.github.io/cloudstream/html/app/com.lagradost.cloudstream3/-tv-type/index.html
    tvTypes = listOf(
        "Movie",
        "TvSeries",
        "Anime"
    )
}
