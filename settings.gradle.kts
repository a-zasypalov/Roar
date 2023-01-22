pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Roar"
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
