plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
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

tasks.register("make") {
    doLast {
        val buildDir = file("$rootDir/build/plugins")
        buildDir.mkdirs()
        
        File(buildDir, "DiziDay.cs3").writeBytes(
            File(layout.buildDirectory.get().asFile, "outputs/aar/${project.name}-debug.aar")
                .readBytes()
        )
    }
}
