plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("roar-feature-module")
}

android {
    namespace = "com.gaoyun.feature_home_screen"
}

dependencies {
    implementation(projects.androidApp.common)
    implementation(projects.androidApp.featurePetScreen)
}
