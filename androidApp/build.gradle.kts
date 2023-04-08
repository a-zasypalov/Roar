plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    kotlin("android")
}

android {
    namespace = "com.gaoyun.roar.android"
    compileSdk = 33
    defaultConfig {
        applicationId = "com.gaoyun.roar.android"
        minSdk = 26
        targetSdk = 33
        versionCode = 2
        versionName = "1.0.1"
        multiDexEnabled = true
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0"
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

    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("androidx.multidex:multidex:2.0.1")
}