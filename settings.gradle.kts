rootProject.name = "turkce-cloudstream"

// Eklentileri include et
include(":FilmEkseni")
include(":DiziDay")

// Bu kısmı ekleyin
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
