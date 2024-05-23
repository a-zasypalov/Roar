plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
    id("com.squareup.sqldelight")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

kotlin {
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

    jvmToolchain(17)

    sqldelight {
        database("RoarDatabase") {
            packageName = "com.gaoyun.roar.model.entity"
            sourceFolders = listOf("kotlin")
            dialect = "sqlite:3.24"
            version = 3
        }
        linkSqlite = true
    }

    val coroutinesVersion = "1.8.1"
    val serializationVersion = "1.6.3"
    val ktorVersion = "2.3.11"
    val koinVersion = "3.5.6"
    val lifecycleVersion = "2.8.0"
    val precomposeVersion = "1.6.0"

    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")

                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

                implementation("io.insert-koin:koin-core:$koinVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
                implementation("com.squareup.sqldelight:runtime:1.5.5")

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.components.resources)
                implementation(compose.materialIconsExtended)
                implementation("org.jetbrains.androidx.lifecycle:lifecycle-common:$lifecycleVersion")
                implementation("org.jetbrains.androidx.lifecycle:lifecycle-runtime-compose:$lifecycleVersion")

                implementation("moe.tlaster:precompose:$precomposeVersion")
                implementation("moe.tlaster:precompose-viewmodel:$precomposeVersion")
                implementation("moe.tlaster:precompose-koin:$precomposeVersion")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation("io.insert-koin:koin-core:$koinVersion")
                implementation("io.ktor:ktor-client-mock:$ktorVersion")
                implementation("io.insert-koin:koin-test:$koinVersion")
            }
        }
        androidMain {
            dependencies {
                implementation("io.insert-koin:koin-android:$koinVersion")
                implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
                implementation("com.squareup.sqldelight:android-driver:1.5.5")
                implementation("androidx.work:work-runtime-ktx:2.9.0")

                implementation("com.google.firebase:firebase-common-ktx:21.0.0")
                implementation("com.google.firebase:firebase-storage-ktx:21.0.0")
            }
        }

        iosMain {
            dependencies {
                implementation("io.ktor:ktor-client-ios:$ktorVersion")
                implementation("com.squareup.sqldelight:native-driver:1.5.5")
            }
        }
    }
}

android {
    namespace = "com.gaoyun.roar"
    compileSdk = 34
    defaultConfig {
        minSdk = 26
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}