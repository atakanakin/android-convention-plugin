import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            extensions.configure<LibraryExtension> {
                compileSdk = 36

                defaultConfig {
                    minSdk = 24
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                buildTypes {
                    release {
                        isMinifyEnabled = false
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }

                lint {
                    warningsAsErrors = false
                    abortOnError = true
                    checkReleaseBuilds = true
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }

                buildFeatures {
                    compose = true
                }
            }

            extensions.configure<KotlinProjectExtension> {
                jvmToolchain(17)
            }

            tasks.configureEach {
                // Tüm lint görevlerini bul
                if (name.startsWith("lint", ignoreCase = true)) {
                    // internalDebug varyantını devre dışı bırak
                    if (name.contains("internalDebug", ignoreCase = true)) {
                        enabled = false
                    } else {
                        // Diğer lint görevleri normal şekilde çalışsın
                        // (İstersen buraya check veya assemble dependency ekleyebilirsin)
                    }
                }
            }
        }
    }
}