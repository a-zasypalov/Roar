plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("roar-feature-module")
}

android {
    namespace = "com.gaoyun.feature_interactions"
}

dependencies {
    implementation(projects.androidApp.common)
}
