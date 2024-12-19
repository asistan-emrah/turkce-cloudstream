// use an integer for version numbers
version = 1


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
    defaultConfig {
        manifestPlaceholders["pluginClassName"] = "com.diziday.DizidayProvider"
    }
}

dependencies {
    implementation("com.lagradost:cloudstream3:pre-release")
}

