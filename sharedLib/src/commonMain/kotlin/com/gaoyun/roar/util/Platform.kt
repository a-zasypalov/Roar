package com.gaoyun.roar.util

expect object Platform {
    val name: PlatformNames
    val supportsDynamicColor: Boolean
}

enum class PlatformNames {
    Android, IOS
}