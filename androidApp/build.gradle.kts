plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.crashlytics)
    alias(libs.plugins.compose.compiler)
}

android {

    val versionMajor = 1
    val versionMinor = 1
    val versionPatch = 0

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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
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
    implementation(projects.sharedLib)
    implementation(libs.androidx.activity)
    implementation(libs.bundles.appupdate)
    implementation(libs.androidx.material)
    implementation(libs.compose.material3)
    implementation(libs.firebase.bom)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.auth.ui)
    implementation(libs.compose.uiTooling.preview)
    implementation(libs.compose.uiTooling)
    implementation(libs.androidx.browser)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx)
    implementation(libs.koin.compose)
    implementation(libs.koin.workmanager)
    implementation(libs.androidx.work.runtime)
    implementation(libs.kotlin.datetime)
    implementation(libs.appupdate)
    implementation(libs.appupdate.ktx)
}