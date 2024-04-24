import com.android.build.gradle.ProguardFiles.ProguardFile

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("roar-feature-module")
}

android {
    namespace = "com.gaoyun.notifications"


    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile(ProguardFile.OPTIMIZE.fileName), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(projects.androidApp.common)
}
