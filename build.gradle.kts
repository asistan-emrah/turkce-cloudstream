plugins {
    id("com.android.library")
    kotlin("android")
    id("maven-publish")
    kotlin("kapt") 
}

android {
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    namespace = "com.lagradost.cloudstream3.extractors" 

    buildFeatures {
        viewBinding = true 
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.github.joreilly:PeopleInSpace:3.1.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("org.jsoup:jsoup:1.15.3") // HTML parsing
    implementation("com.squareup.okhttp3:okhttp:4.10.0") // HTTP requests

    implementation("com.lagradost.cloudstream3:core:1.0.0") // Cloudstream Core kütüphanesi

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.+")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("androidx.room:room-runtime:2.4.3")
    kapt("androidx.room:room-compiler:2.4.3")
    implementation("com.github.bumptech.glide:glide:4.13.2")
    kapt("com.github.bumptech.glide:compiler:4.13.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.preference:preference-ktx:1.2.0")
    implementation("androidx.media:media:1.6.0")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}

afterEvaluate {
    publishing {
        publications {
            release(MavenPublication) {
                from(components["release"])
                groupId = "com.github.asistan-emrah"
                artifactId = "cloudstream"
                version = "1.0.0"
            }
        }
    }
}
