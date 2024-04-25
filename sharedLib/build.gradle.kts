plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.sqldelight)
}

android {
    namespace = "com.gaoyun.roar"
    compileSdk = 34
    defaultConfig {
        minSdk = 26
    }
}

sqldelight {
    database("RoarDatabase") {
        packageName = "com.gaoyun.roar.model.entity"
        sourceFolders = listOf("kotlin")
        dialect = "sqlite:3.24"
        version = 3
    }
    linkSqlite = true
}

kotlin {
    applyDefaultHierarchyTemplate()
    // Targets
    androidTarget()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "sharedLib"
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.coroutines.core)
            implementation(libs.kotlin.serialization.core)
            implementation(libs.kotlin.datetime)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.serialization)
            implementation(libs.ktor.serialization.json)
            implementation(libs.ktor.logging)
            implementation(libs.ktor.cio)
            implementation(libs.ktor.contentnegotiation)
            implementation(libs.koin.core)
            implementation(libs.sqldelight.runtime)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.ktor.clientmock)
            implementation(libs.koin.core)
            implementation(libs.koin.test)
        }
        androidMain.dependencies {
            implementation(libs.androidx.lifecycle.runtime)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.work.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.material2)
            implementation(libs.koin.android)
            implementation(libs.sqldelight.androidDriver)
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.common)
            implementation(libs.firebase.storage)
            implementation(libs.loggingInterceptor)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.ios)
            implementation(libs.sqldelight.nativeDriver)
        }
    }
}
