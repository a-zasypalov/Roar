@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    kotlin("android")
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
        multiDexEnabled = true
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":sharedLib"))
    implementation(project(":androidApp:common"))
    implementation(project(":androidApp:notifications"))

    implementation(project(":androidApp:feature-home-screen"))
    implementation(project(":androidApp:feature-user-registration"))
    implementation(project(":androidApp:feature-add-pet"))
    implementation(project(":androidApp:feature-pet-screen"))
    implementation(project(":androidApp:feature-create-reminder"))
    implementation(project(":androidApp:feature-interactions"))
    implementation(project(":androidApp:feature-user-screen"))
    implementation(project(":androidApp:feature-onboarding"))

    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.multidex:multidex:2.0.1")

    implementation("com.google.android.play:app-update:2.1.0")
    implementation("com.google.android.play:app-update-ktx:2.1.0")
}