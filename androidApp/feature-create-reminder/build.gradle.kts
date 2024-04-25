plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("roar-feature-module")
}

android {
    namespace = "com.gaoyun.feature_create_reminder"
}

dependencies {
    implementation(projects.androidApp.common)
    implementation(projects.androidApp.notifications)
}
