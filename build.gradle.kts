plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.lagradost.cloudstream3.gradle") version "2.0"
}

android {
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("com.lagradost:cloudstream3:pre-release")
}

cloudstream {
    language = "tr"
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
    tvTypes = listOf(
        "Movie",
        "TvSeries",
        "Anime"
    )
}
