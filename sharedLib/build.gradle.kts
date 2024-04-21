plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
    id("com.squareup.sqldelight")
    id("org.jetbrains.compose")
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

    val coroutinesVersion = "1.8.0"
    val serializationVersion = "1.6.3"
    val ktorVersion = "2.3.10"
    val koinVersion = "3.5.6"
    val lifecycleVersion = "2.7.0"
    val precomposeVersion = "1.6.0"

    sourceSets {
        val commonMain by getting {
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
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
                implementation("com.squareup.sqldelight:runtime:1.5.5")

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                implementation(compose.materialIconsExtended)

                implementation("moe.tlaster:precompose:$precomposeVersion")
                implementation("moe.tlaster:precompose-viewmodel:$precomposeVersion")
                implementation("moe.tlaster:precompose-koin:$precomposeVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("io.insert-koin:koin-core:$koinVersion")
                implementation("io.ktor:ktor-client-mock:$ktorVersion")
                implementation("io.insert-koin:koin-test:$koinVersion")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("io.insert-koin:koin-android:$koinVersion")
                implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
                implementation("com.squareup.sqldelight:android-driver:1.5.5")
                implementation("androidx.work:work-runtime-ktx:2.9.0")


                val composeVersion = "1.6.5"
                implementation("androidx.compose.ui:ui:$composeVersion")
                implementation("androidx.compose.material:material:$composeVersion")

                implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
                implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")

                implementation("com.google.firebase:firebase-common-ktx:20.4.3")
                implementation("com.google.firebase:firebase-storage-ktx:20.3.0")
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
                implementation("com.squareup.sqldelight:native-driver:1.5.5")
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
    compileSdk = 34
    defaultConfig {
        minSdk = 26
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}