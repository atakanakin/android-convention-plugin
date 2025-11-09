import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class SpotlessConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.diffplug.spotless")

            extensions.configure<SpotlessExtension> {
                kotlin {
                    target("**/*.kt")
                    targetExclude("**/build/**/*.kt")

                    ktlint("1.5.0")
                        .editorConfigOverride(
                            mapOf(
                                "max_line_length" to "120",
                                "ktlint_function_naming_ignore_when_annotated_with" to "Composable",
                            )
                        )
                    trimTrailingWhitespace()
                    endWithNewline()
                }

                kotlinGradle {
                    target("**/*.gradle.kts")
                    targetExclude("**/build/**/*.gradle.kts")

                    ktlint("1.5.0")
                        .editorConfigOverride(
                            mapOf(
                                "max_line_length" to "120",
                                "ktlint_standard_no-wildcard-imports" to "disabled",
                            )
                        )
                    trimTrailingWhitespace()
                    endWithNewline()
                }

                format("xml") {
                    target("**/*.xml")
                    targetExclude("**/build/**/*.xml")

                    trimTrailingWhitespace()
                    endWithNewline()
                }
            }
            afterEvaluate {
                tasks.configureEach {
                    if (name.startsWith("assemble", ignoreCase = true)) {
                        val variantName = name.removePrefix("assemble")

                        if (variantName.equals("internalDebug", ignoreCase = true)) {
                            return@configureEach
                        }

                        if (variantName.contains("prod", ignoreCase = true) ||
                            variantName.contains("release", ignoreCase = true)
                        ) {
                            tasks.findByName("spotlessCheck")?.let { spotlessTask ->
                                dependsOn(spotlessTask)
                            }
                        }
                    }
                }
            }
        }
    }
}
