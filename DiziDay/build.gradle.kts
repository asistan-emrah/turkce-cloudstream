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

    buildTypes {
        release {
            isMinifyEnabled = false
        }
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
android.applicationVariants.all {
    outputs.all {
        val outputFileName = "DiziDay.cs3"
        file("$rootDir/build/plugins").mkdirs()
        file("$rootDir/build/plugins/$outputFileName").delete()
        outputFile.renameTo(file("$rootDir/build/plugins/$outputFileName"))
    }
}
