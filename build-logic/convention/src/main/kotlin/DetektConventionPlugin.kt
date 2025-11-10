import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("io.gitlab.arturbosch.detekt")

            extensions.configure<DetektExtension> {
                val userConfig = file("$rootDir/config/detekt/detekt.yml")
                if (userConfig.exists()) {
                    config.setFrom(files(userConfig))
                } else {
                    val defaultConfig = javaClass.getResource("/config/detekt/detekt.yml")
                    config.setFrom(
                        files(
                            defaultConfig?.path ?: "$rootDir/config/detekt/detekt.yml"
                        )
                    )
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