plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.gaoyun.common"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    api(project(":sharedLib"))
    api("androidx.core:core-ktx:1.13.1")
    api("androidx.appcompat:appcompat:1.6.1")
    api("com.google.android.material:material:1.12.0")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.8.1")

//    api("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    api("androidx.activity:activity-compose:1.9.0")

    val composeVersion = "1.6.7"
    api("androidx.compose.ui:ui:$composeVersion")
    api("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    api("androidx.compose.material:material-icons-extended:$composeVersion")
    api("androidx.navigation:navigation-compose:2.7.7")

    api("androidx.compose.material3:material3:1.2.1")

    api("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")

    api("com.google.accompanist:accompanist-permissions:0.34.0")
    api("com.google.accompanist:accompanist-insets:0.30.1")
    api("com.google.accompanist:accompanist-systemuicontroller:0.32.0")

    api("androidx.work:work-runtime-ktx:2.9.0")

    val koinVersion = "3.5.6"
    api("io.insert-koin:koin-android:$koinVersion")
    api("io.insert-koin:koin-android-ext:3.0.2")
    api("io.insert-koin:koin-androidx-compose:$koinVersion")
    api("io.insert-koin:koin-androidx-workmanager:$koinVersion")

    api("io.coil-kt:coil:2.6.0")
    api("io.coil-kt:coil-compose:2.6.0")

    api("androidx.browser:browser:1.8.0")

    debugApi("androidx.compose.ui:ui-tooling:1.6.7")
    api("androidx.compose.ui:ui-tooling-preview:1.6.7")

    api(platform("com.google.firebase:firebase-bom:33.0.0"))
    api("com.google.firebase:firebase-crashlytics-ktx")
    api("com.google.firebase:firebase-analytics-ktx")
    api("com.google.firebase:firebase-messaging-ktx")
    api("com.firebaseui:firebase-ui-auth:7.2.0")
    api("com.google.firebase:firebase-auth-ktx:23.0.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}