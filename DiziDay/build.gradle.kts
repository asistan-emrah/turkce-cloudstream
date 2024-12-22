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

afterEvaluate {
    android.libraryVariants.all {
        val variantName = name
        val outputFileName = "DiziDay.cs3"

        tasks.register("make${variantName.capitalize()}Jar", Jar::class) {
            dependsOn("assemble")
            from("${buildDir}/intermediates/aar_main_jar/${variantName}/classes.jar")
            archiveFileName.set(outputFileName)
            destinationDirectory.set(File(rootProject.buildDir, "plugins"))
        }
    }
}
