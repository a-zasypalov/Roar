plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("roar-feature-module")
}

android {
    namespace = "com.gaoyun.pet_screen"
}

dependencies {
    implementation(projects.androidApp.common)
}
