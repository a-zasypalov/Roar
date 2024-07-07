enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Roar"

includeBuild("build-logic")
include(":androidApp")
include(":sharedLib")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://www.jetbrains.com/intellij-repository/releases")
    }

    plugins {
        id("org.jetbrains.compose") version "1.6.10" apply false
    }
}

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
