import com.android.build.gradle.BaseExtension
import java.io.File

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

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

fun Project.android(configure: BaseExtension.() -> Unit) = extensions.configure("android", configure)

subprojects {
    apply(plugin = "com.android.library")
    apply(plugin = "kotlin-android")

    android {
        compileSdkVersion(33)
        defaultConfig {
            minSdkVersion(21)
            targetSdkVersion(33)
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
        tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions {
                jvmTarget = "1.8"
                freeCompilerArgs = listOf("-Xjvm-default=compatibility")
            }
        }
    }

    dependencies {
        implementation("com.lagradost:cloudstream3:pre-release")
    }
}

task("make") {
    dependsOn(":FilmEkseni:make")
    dependsOn(":DiziDay:make")
}

// Her bir eklenti için alt proje oluştur
listOf("FilmEkseni", "DiziDay").forEach { pluginName ->
    include(":$pluginName")
    project(":$pluginName").apply {
        val projectPath = projectDir.absolutePath
        
        // build.gradle.kts dosyasını oluştur
        File("$projectPath/build.gradle.kts").writeText("""
            // Eklenti versiyonu
            version = 1
            
            cloudstream {
                // Eklenti özellikleri
                language = "tr"
                description = "${pluginName} için CloudStream eklentisi"
                authors = listOf("asistan-emrah")
                
                // Eklenti durumu (0: Down, 1: Ok, 2: Slow, 3: Beta)
                status = 1
                
                // Desteklenen içerik türleri
                tvTypes = listOf(
                    ${if(pluginName == "FilmEkseni") "\"Movie\"" else "\"TvSeries\""}
                )
            }
            
            android {
                defaultConfig {
                    applicationId = "com.cloudstream.${pluginName.toLowerCase()}"
                }
            }
        """.trimIndent())
        
        // settings.gradle.kts dosyasını oluştur
        File("$projectPath/settings.gradle.kts").writeText("")
        
        // Provider sınıfını doğru konuma taşı
        File("$projectPath/src/main/kotlin").mkdirs()
        if (pluginName == "FilmEkseni") {
            File("FilmEkseniProvider.kt").copyTo(
                File("$projectPath/src/main/kotlin/FilmEkseniProvider.kt")
            )
        } else {
            File("DiziDayProvider.kt").copyTo(
                File("$projectPath/src/main/kotlin/DiziDayProvider.kt")
            )
        }
    }
}

