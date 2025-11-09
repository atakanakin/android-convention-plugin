import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationFlavorsConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<ApplicationExtension> {
                flavorDimensions += "environment"

                productFlavors {
                    create("prod") {
                        dimension = "environment"
                        applicationIdSuffix = null
                    }

                    create("internal") {
                        dimension = "environment"
                        applicationIdSuffix = ".internal"
                    }
                }
            }
        }
    }
}