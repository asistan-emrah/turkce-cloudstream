buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    }
}

tasks.register("make") {
    dependsOn(":FilmEkseni:makeDebugJar", ":DiziDay:makeDebugJar")
}
