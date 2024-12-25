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

    namespace = "com.cloudstream3.diziday"
}

dependencies {
    implementation("com.lagradost:cloudstream3:pre-release")
}

// CloudStream yapılandırması
android.applicationVariants.all {
    val variantName = name
    val outputFileName = "Diziday.cs3"

    outputs.all {
        val output = this as? com.android.build.gradle.internal.api.BaseVariantOutputImpl
        output?.outputFileName = outputFileName
    }
}
