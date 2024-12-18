import com.lagradost.cloudstream3.gradle.CloudstreamExtension
import com.android.build.gradle.BaseExtension

buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath("com.github.recloudstream:gradle:master-SNAPSHOT")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    }
}

plugins {
    id("com.android.library")
    id("kotlin-android")
}

apply(from = "https://raw.githubusercontent.com/recloudstream/gradle/master/plugins.gradle")

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

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

cloudstream {
    // Eklenti adını açıkça belirtin
    pluginName = "FilmekseniProvider"
    description = "Filmekseni için Cloudstream eklentisi"
    language = "tr"
    authors = listOf("asistan-emrah")
    status = 1
    tvTypes = listOf(
        "Movie",
        "TvSeries"
    )
}

dependencies {
    implementation("com.github.Blatzar:NiceHttp:0.4.4")
    implementation("org.jsoup:jsoup:1.15.3")
    implementation("com.google.android.exoplayer:exoplayer-core:2.18.5")
    implementation("com.google.android.exoplayer:exoplayer-hls:2.18.5")
    implementation("com.google.android.exoplayer:exoplayer-dash:2.18.5")
}
