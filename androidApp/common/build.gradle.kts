plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.gaoyun.common"
    compileSdk = 32

    defaultConfig {
        minSdk = 26
        targetSdk = 32

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    api(project(":sharedLib"))
    api("androidx.core:core-ktx:1.7.0")
    api("androidx.appcompat:appcompat:1.4.1")
    api("com.google.android.material:material:1.5.0")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.1-native-mt")

    api("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    api("androidx.activity:activity-compose:1.4.0")

    val composeVersion = "1.1.1"
    api("androidx.compose.ui:ui:$composeVersion")
    api("androidx.compose.material:material:$composeVersion")
    api("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    api("androidx.compose.material:material-icons-extended:$composeVersion")
    api("androidx.navigation:navigation-compose:2.4.2")

    api("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")
    api("com.google.accompanist:accompanist-permissions:0.24.7-alpha")

    val koinVersion = "3.1.6"
    api("io.insert-koin:koin-android:$koinVersion")
    api("io.insert-koin:koin-android-ext:3.0.2")
    api("io.insert-koin:koin-androidx-compose:$koinVersion")

    api("io.coil-kt:coil:2.0.0-rc03")
    api("io.coil-kt:coil-compose:2.0.0-rc03")

    api("androidx.browser:browser:1.4.0")

    debugApi("androidx.compose.ui:ui-tooling:1.1.1")
    api("androidx.compose.ui:ui-tooling-preview:1.1.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}