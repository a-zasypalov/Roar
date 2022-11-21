plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.gaoyun.roar.android"
    compileSdk = 33
    defaultConfig {
        applicationId = "com.gaoyun.roar.android"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
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
    implementation(project(":androidApp:feature-home-screen"))
    implementation(project(":androidApp:feature-user-registration"))
    implementation(project(":androidApp:feature-add-pet"))
    implementation(project(":androidApp:feature-pet-screen"))
    implementation(project(":androidApp:feature-create-reminder"))

    implementation("androidx.activity:activity-compose:1.6.1")
}