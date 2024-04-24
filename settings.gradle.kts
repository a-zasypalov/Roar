enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Roar"

includeBuild("build-logic")
include(":androidApp")
include(":sharedLib")
include(":androidApp:common")
include(":androidApp:feature-user-registration")
include(":androidApp:feature-home-screen")
include(":androidApp:feature-add-pet")
include(":androidApp:feature-create-reminder")
include(":androidApp:feature-pet-screen")
include(":androidApp:feature-interactions")
include(":androidApp:feature-user-screen")
include(":androidApp:notifications")
include(":androidApp:feature-onboarding")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://www.jetbrains.com/intellij-repository/releases")
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
