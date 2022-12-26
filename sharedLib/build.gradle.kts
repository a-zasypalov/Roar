plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
    id("com.squareup.sqldelight")
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
        }
    }

    val coroutinesVersion = "1.6.1-native-mt"
    val serializationVersion = "1.3.2"
    val ktorVersion = "1.6.8"
    val koinVersion = "3.1.6"
    val lifecycleVersion = "2.5.1"

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
                implementation("io.insert-koin:koin-core:$koinVersion")
                implementation("androidx.work:work-runtime-ktx:2.7.1")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                implementation("com.squareup.sqldelight:runtime:1.5.4")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("io.insert-koin:koin-core:$koinVersion")
                implementation("io.ktor:ktor-client-mock:$ktorVersion")
                implementation("io.insert-koin:koin-test:$koinVersion")
                implementation("io.kotest:kotest-framework-engine:5.3.0")
                implementation("io.kotest:kotest-assertions-core:5.3.0")
                implementation("io.kotest:kotest-property:5.3.0")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("io.insert-koin:koin-android:$koinVersion")
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
                implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
                implementation("com.squareup.sqldelight:android-driver:1.5.4")

                val composeVersion = "1.1.1"
                implementation("androidx.compose.ui:ui:$composeVersion")
                implementation("androidx.compose.material:material:$composeVersion")

                implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
                implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("io.mockk:mockk-common:1.13.2")
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
                implementation("io.ktor:ktor-client-ios:$ktorVersion")
                implementation("com.squareup.sqldelight:native-driver:1.5.3")
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

android {
    namespace = "com.gaoyun.roar"
    compileSdk = 33
    defaultConfig {
        minSdk = 26
        targetSdk = 33
    }
}