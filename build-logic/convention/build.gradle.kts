plugins {
    `kotlin-dsl`
    `maven-publish`
}

val conventionGroup: String by project
val conventionArtifact: String by project
val conventionVersion: String by project

group = conventionGroup
version = conventionVersion

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.detekt.gradlePlugin)
    implementation(libs.spotless.gradlePlugin)
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
            groupId = conventionGroup
            artifactId = conventionArtifact
            version = conventionVersion
            
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
