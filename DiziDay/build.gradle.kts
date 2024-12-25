plugins {
    id("com.android.library")
    id("kotlin-android")
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

// CloudStream yapılandırması
android.libraryVariants.all {
    outputs.all {
        (this as com.android.build.gradle.internal.api.BaseVariantOutputImpl).outputFileName = "DiziDay.cs3"
    }
}

extra["cloudstreamLanguage"] = "tr"
extra["cloudstreamDescription"] = "DiziDay için CloudStream eklentisi"
extra["cloudstreamAuthors"] = listOf("asistan-emrah")
extra["cloudstreamStatus"] = 1
extra["cloudstreamTvTypes"] = listOf("Movie", "TvSeries", "Anime")
