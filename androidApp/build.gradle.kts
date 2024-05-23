@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("org.jetbrains.kotlin.plugin.compose")
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
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":sharedLib"))

    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.multidex:multidex:2.0.1")

    implementation("com.google.android.play:app-update:2.1.0")
    implementation("com.google.android.play:app-update-ktx:2.1.0")

    implementation("com.google.android.material:material:1.12.0")

    implementation("androidx.compose.material3:material3:1.2.1")

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")

    implementation("androidx.work:work-runtime-ktx:2.9.0")

    val koinVersion = "3.5.6"
    implementation("io.insert-koin:koin-android:$koinVersion")
    implementation("io.insert-koin:koin-android-ext:3.0.2")
    implementation("io.insert-koin:koin-androidx-compose:$koinVersion")
    implementation("io.insert-koin:koin-androidx-workmanager:$koinVersion")

    implementation("androidx.browser:browser:1.8.0")

    debugImplementation("androidx.compose.ui:ui-tooling:1.6.7")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.7")

    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.firebaseui:firebase-ui-auth:7.2.0")
    implementation("com.google.firebase:firebase-auth-ktx:23.0.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}