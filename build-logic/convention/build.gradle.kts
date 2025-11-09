plugins {
    `kotlin-dsl`
}

group = "dev.atakanakin.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
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
    }
}