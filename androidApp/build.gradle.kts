@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.crashlytics)
}

android {

    val versionMajor = 1
    val versionMinor = 0
    val versionPatch = 1

    val versionCodeValue = versionMajor * 10000 + versionMinor * 100 + versionPatch
    val versionNameValue = "${versionMajor}.${versionMinor}.${versionPatch}"

    namespace = "com.gaoyun.roar.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.gaoyun.roar.android"
        minSdk = 26
        targetSdk = 34
        versionCode = versionCodeValue
        versionName = versionNameValue
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(projects.sharedLib)
    implementation(projects.androidApp.common)
    implementation(projects.androidApp.notifications)

    implementation(projects.androidApp.featureHomeScreen)
    implementation(projects.androidApp.featureUserRegistration)
    implementation(projects.androidApp.featureAddPet)
    implementation(projects.androidApp.featurePetScreen)
    implementation(projects.androidApp.featureCreateReminder)
    implementation(projects.androidApp.featureInteractions)
    implementation(projects.androidApp.featureUserScreen)
    implementation(projects.androidApp.featureOnboarding)

    implementation(libs.androidx.activity)

    implementation(libs.bundles.appupdate)
}
