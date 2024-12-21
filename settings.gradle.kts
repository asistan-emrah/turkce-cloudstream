rootProject.name = "turkce-cloudstream"

include(":FilmEkseni", ":DiziDay")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io")
    }
}
