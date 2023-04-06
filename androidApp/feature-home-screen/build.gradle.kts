plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.gaoyun.feature_home_screen"
    compileSdk = 33

    defaultConfig {
        minSdk = 26
        targetSdk = 33
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0"
    }
}

dependencies {
    implementation(project(":androidApp:common"))
    implementation(project(":androidApp:feature-pet-screen"))
    implementation("com.firebaseui:firebase-ui-auth:7.2.0")
    implementation("com.google.firebase:firebase-auth-ktx:21.2.0")
}