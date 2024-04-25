plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("roar-feature-module")
}

android {
    namespace = "com.gaoyun.feature_onboarding"
}

dependencies {
    implementation(projects.androidApp.common)
    implementation(libs.accompanist.pager)
}
