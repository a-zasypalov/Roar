plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("roar-feature-module")
}

android {
    namespace = "com.gaoyun.common"
}

dependencies {
    api(projects.sharedLib)

    api(libs.androidx.core)
    api(libs.androidx.appcompat)
    api(libs.androidx.activity)
    api(libs.androidx.browser)
    api(libs.androidx.material)
    api(libs.androidx.navigation)
    api(libs.androidx.lifecycle.runtime)
    api(libs.androidx.work.runtime)
    api(libs.kotlin.coroutines.core)
    api(libs.kotlin.datetime)

    api(libs.compose.ui)
    api(libs.compose.materialIcons)
    api(libs.compose.material3)
    api(libs.compose.uiTooling.preview)
    debugApi(libs.compose.uiTooling)

    api(libs.accompanist.permissions)
    api(libs.accompanist.insets)
    api(libs.accompanist.uicontroller)

    api(libs.koin.android)
    api(libs.koin.androidx)
    api(libs.koin.compose)
    api(libs.koin.workmanager)

    api(libs.coil)
    api(libs.coil.compose)

    api(platform(libs.firebase.bom))
    api(libs.firebase.auth)
    api(libs.firebase.auth.ui)
    api(libs.firebase.analytics)
    api(libs.firebase.crashlytics)
    api(libs.firebase.messaging)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junitx)
    androidTestImplementation(libs.espresso)
}
