plugins {
    `kotlin-dsl`
    `maven-publish`
}

group = "dev.atakanakin.convention"
version = "1.0.0"

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

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "dev.atakanakin.convention"
            artifactId = "android-plugins"
            version = "1.0.0"
            
            from(components["java"])
        }
    }
    
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/atakanakin/android-convention-plugin")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: project.findProperty("gpr.user") as String?
                password = System.getenv("GITHUB_TOKEN") ?: project.findProperty("gpr.token") as String?
            }
        }
    }
}
