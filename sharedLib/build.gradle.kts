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
        targetSdk = 34
    }
}

kotlin {
    android()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "sharedLib"
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

    sourceSets {
        val commonMain by getting {
            dependencies {
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
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.ktor.clientmock)
                implementation(libs.koin.core)
                implementation(libs.koin.test)
            }
        }
        val androidMain by getting {
            dependencies {
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
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(libs.ktor.client.ios)
                implementation(libs.sqldelight.nativeDriver)
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}
