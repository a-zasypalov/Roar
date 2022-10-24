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
