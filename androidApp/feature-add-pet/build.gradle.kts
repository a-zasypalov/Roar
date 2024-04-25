plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("roar-feature-module")
}

android {
    namespace = "com.example.feature_add_pet"
}

dependencies {
    implementation(projects.androidApp.common)
}
