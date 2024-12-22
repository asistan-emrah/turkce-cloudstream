buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    }
}

plugins {
    id("com.android.library") apply false
    id("org.jetbrains.kotlin.android") apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

tasks.register("make") {
    dependsOn(":FilmEkseni:makeDebugJar", ":DiziDay:makeDebugJar")
}
