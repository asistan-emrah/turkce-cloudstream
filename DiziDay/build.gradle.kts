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
    implementation("com.github.recloudstream:cloudstream:pre-release")
}

tasks.register<Jar>("makeDebugJar") {
    dependsOn("assembleDebug")
    from("$buildDir/intermediates/aar_main_jar/debug/classes.jar")
    archiveFileName.set("DiziDay.cs3")
    destinationDirectory.set(File(rootProject.buildDir, "plugins"))
}
