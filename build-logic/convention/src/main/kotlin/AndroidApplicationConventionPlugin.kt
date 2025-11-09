import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            extensions.configure<ApplicationExtension> {
                compileSdk = 36

                defaultConfig {
                    minSdk = 24
                    targetSdk = 36
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

            afterEvaluate {
                tasks.configureEach {
                    if (name.startsWith("lint", ignoreCase = true) &&
                        name.contains("internalDebug", ignoreCase = true)
                    ) {
                        enabled = false
                    }
                }
                tasks.configureEach {
                    if (name.startsWith("assemble", ignoreCase = true)) {
                        val variantName = name.removePrefix("assemble")
                        if (variantName.equals(
                                "internalDebug",
                                ignoreCase = true
                            )
                        ) return@configureEach

                        val lintTaskName = "lint$variantName"
                        tasks.findByName(lintTaskName)?.let { lintTask ->
                            dependsOn(lintTask)
                        }
                    }
                }
            }
        }
    }
}