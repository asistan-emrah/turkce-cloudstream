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

    namespace = "com.cloudstream3.filmekseni"
}

dependencies {
    implementation("com.lagradost:cloudstream3:pre-release")
}

cloudstream {
    language = "tr"
    description = "FilmEkseni için CloudStream eklentisi"
    authors = listOf("asistan-emrah")
    status = 1 // 1 OK anlamına gelir
    tvTypes = listOf("Movie", "TvSeries", "Anime")
}
