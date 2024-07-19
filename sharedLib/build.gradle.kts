import org.jetbrains.kotlin.gradle.plugin.mpp.BitcodeEmbeddingMode

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

val appId = "com.gaoyun.roar"

android {
    namespace = appId
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
        iosArm64(),
        iosSimulatorArm64(),
        iosX64()
    ).forEach {
        it.binaries.framework {
            baseName = "sharedLib"
            isStatic = true
            embedBitcode(BitcodeEmbeddingMode.DISABLE)
            linkerOpts("-lsqlite3", "-application_extension", "-ld64")
            binaryOption("bundleId", "${appId}.sharedLib")
            freeCompilerArgs += listOf("-Xoverride-konan-properties=minVersion.ios=14.0.0")
        }
    }

    jvmToolchain(17)

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
            implementation(libs.lifecycle.common)
            implementation(libs.lifecycle.common.runtime)
            implementation(libs.precompose)
            implementation(libs.precompose.viewmodel)
            implementation(libs.precompose.koin)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.uiToolingPreview)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.ktor.clientmock)
            implementation(libs.koin.core)
            implementation(libs.koin.test)
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity.ktx)
            implementation(libs.androidx.lifecycle.runtime)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.work.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.material3)
            implementation(libs.koin.android)
            implementation(libs.sqldelight.androidDriver)
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.common)
            implementation(libs.firebase.storage)
            implementation(libs.firebase.auth)
            implementation(libs.loggingInterceptor)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.ios)
            implementation(libs.sqldelight.nativeDriver)
        }
    }
}
