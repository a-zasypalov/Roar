plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.gaoyun.common"
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
    api(project(":sharedLib"))
    api("androidx.core:core-ktx:1.9.0")
    api("androidx.appcompat:appcompat:1.5.1")
    api("com.google.android.material:material:1.7.0")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.4")

    api("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    api("androidx.activity:activity-compose:1.6.1")

    val composeVersion = "1.1.1"
    api("androidx.compose.ui:ui:$composeVersion")
    api("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    api("androidx.compose.material:material-icons-extended:$composeVersion")
    api("androidx.navigation:navigation-compose:2.5.3")

    api("androidx.compose.material3:material3:1.0.1")

    api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

    api("com.google.accompanist:accompanist-permissions:0.27.1")
    api("com.google.accompanist:accompanist-insets:0.27.1")
    api("com.google.accompanist:accompanist-systemuicontroller:0.27.1")

    val koinVersion = "3.1.6"
    api("io.insert-koin:koin-android:$koinVersion")
    api("io.insert-koin:koin-android-ext:3.0.2")
    api("io.insert-koin:koin-androidx-compose:$koinVersion")

    api("io.coil-kt:coil:2.2.2")
    api("io.coil-kt:coil-compose:2.2.2")

    api("androidx.browser:browser:1.4.0")

    debugApi("androidx.compose.ui:ui-tooling:1.3.1")
    api("androidx.compose.ui:ui-tooling-preview:1.3.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
}