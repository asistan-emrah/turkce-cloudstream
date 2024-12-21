buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

tasks.register("make") {
    group = "cloudstream"
    description = "Bu depodaki eklentileri derler"
    
    dependsOn(subprojects.map { it.tasks.named("make") })
}
