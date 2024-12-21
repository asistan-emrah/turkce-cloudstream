rootProject.name = "turkce-cloudstream"

// build.gradle.kts içeren tüm dizinleri dahil et
rootDir.walk()
    .maxDepth(1)
    .filter { it.isDirectory && it != rootDir && File(it, "build.gradle.kts").exists() }
    .forEach { 
        include(":${it.name}")
    }

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io")
    }
}
