import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

private const val FORMATTING = "io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8"
private const val COMPOSE = "io.nlopez.compose.rules:detekt:0.4.23"

class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("io.gitlab.arturbosch.detekt")

            dependencies {
                add("detektPlugins", FORMATTING)
                add("detektPlugins", COMPOSE)
            }

            extensions.configure<DetektExtension> {
                val userConfig = file("$rootDir/config/detekt/detekt.yml")
                
                if (userConfig.exists()) {
                    config.setFrom(files(userConfig))
                } else {
                    val embeddedConfig = javaClass.classLoader.getResourceAsStream("config/detekt/detekt.yml")
                    if (embeddedConfig != null) {
                        val tempConfig = file("${layout.buildDirectory.get()}/tmp/detekt-config/detekt.yml")
                        tempConfig.parentFile.mkdirs()
                        embeddedConfig.use { input ->
                            tempConfig.outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }
                        config.setFrom(files(tempConfig))
                        val embeddedLicense = javaClass.classLoader.getResourceAsStream("config/detekt/license.template")
                        if (embeddedLicense != null) {
                            val tempLicense = file("${layout.buildDirectory.get()}/tmp/detekt-config/license.template")
                            embeddedLicense.use { input ->
                                tempLicense.outputStream().use { output ->
                                    input.copyTo(output)
                                }
                            }
                        }
                    }
                }
                
                buildUponDefaultConfig = true
            }

            tasks.withType(Detekt::class) {
                reports {
                    xml.required.set(true)
                    html.required.set(true)
                    txt.required.set(false)
                    sarif.required.set(false)
                }

                val variantName = name.removePrefix("detekt")

                if (variantName.isEmpty() ||
                    variantName.equals("Main", ignoreCase = true) ||
                    variantName.equals("Test", ignoreCase = true)
                ) {
                    return@withType
                }

                if (variantName.equals("internalDebug", ignoreCase = true)) {
                    enabled = false
                } else {
                    tasks.findByName("spotlessCheck")?.let { spotlessTask ->
                        dependsOn(spotlessTask)
                    }

                    val assembleTaskName = "assemble${variantName}"
                    tasks.findByName(assembleTaskName)?.let { assembleTask ->
                        assembleTask.dependsOn(this)
                    }
                }
            }
        }
    }
}