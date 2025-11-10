# Android Convention Plugin

A collection of Gradle convention plugins for Android projects to centralize common build configurations and reduce code duplication across modules.

## Overview

This library provides reusable Gradle plugins that encapsulate standard Android build configurations, code quality tools, and project flavors. Instead of repeating the same configuration in every module, apply these plugins to maintain consistency across your project.

## Plugins

### `dev.atakanakin.android.application`

Configures Android application modules with common settings:

- Compile SDK: 36
- Min SDK: 24
- Target SDK: 36
- Java compatibility: 17
- Jetpack Compose support
- Lint configuration
- ProGuard setup for release builds

### `dev.atakanakin.android.library`

Configures Android library modules with the same settings as the application plugin, excluding application-specific configurations.

### `dev.atakanakin.android.application.flavors`

Adds product flavors for environment-based builds:

- `prod`: Production build
- `internal`: Internal build with `.internal` application ID suffix

### `dev.atakanakin.detekt`

Integrates Detekt static code analysis:

- Loads custom configuration from `config/detekt/detekt.yml`
- Generates XML and HTML reports
- Runs after Spotless formatting checks

### `dev.atakanakin.spotless`

Configures code formatting using Spotless and ktlint:

- Kotlin and Gradle files formatting with ktlint 1.5.0
- XML file formatting
- Max line length: 120 characters
- Automatic trailing whitespace removal

## Usage

### Installation

Add the plugin repository to your `settings.gradle.kts`:

```kotlin
pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.github.com/atakanakin/android-convention-plugin")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
```

The token requires `read:packages` scope to download the plugins.

### Applying Plugins

In your module's `build.gradle.kts`:

```kotlin
plugins {
    id("dev.atakanakin.android.application") version "1.0.0"
    id("dev.atakanakin.android.application.flavors") version "1.0.0"
    id("dev.atakanakin.detekt") version "1.0.0"
    id("dev.atakanakin.spotless") version "1.0.0"
}
```

For library modules:

```kotlin
plugins {
    id("dev.atakanakin.android.library") version "1.0.0"
    id("dev.atakanakin.detekt") version "1.0.0"
    id("dev.atakanakin.spotless") version "1.0.0"
}
```

## Configuration

### Detekt

Place your custom Detekt configuration at `config/detekt/detekt.yml` in your project root. If not found, the plugin will use its embedded default configuration.

The plugin automatically includes Detekt plugins:
- `detekt-formatting`: Code formatting rules
- `detekt-compose`: Jetpack Compose specific rules

### Spotless

The plugin automatically configures Spotless with sensible defaults. To customize ktlint rules, they can be overridden in the plugin configuration.

## Publishing

This plugin is published to GitHub Packages. To publish a new version:

```bash
./gradlew -p build-logic/convention clean build publish
```

Ensure `GITHUB_ACTOR` and `GITHUB_TOKEN` environment variables are set for authentication. The token requires `write:packages` scope.

## Requirements

- Gradle 8.0+
- Android Gradle Plugin 8.13.0
- Kotlin 2.0.21

## License

This project is licensed under the MIT License.
