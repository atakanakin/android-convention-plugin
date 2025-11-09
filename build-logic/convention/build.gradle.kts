plugins {
    `kotlin-dsl`
}

group = "dev.atakanakin.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
    compileOnly(libs.spotless.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "dev.atakanakin.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("androidApplicationFlavors") {
            id = "dev.atakanakin.android.application.flavors"
            implementationClass = "AndroidApplicationFlavorsConventionPlugin"
        }

        register("androidLibrary") {
            id = "dev.atakanakin.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("detekt") {
            id = "dev.atakanakin.detekt"
            implementationClass = "DetektConventionPlugin"
        }

        register("spotless") {
            id = "dev.atakanakin.spotless"
            implementationClass = "SpotlessConventionPlugin"
        }
    }
}