package com.gaoyun.roar.util

expect object Platform {
    val name: PlatformNames
}

enum class PlatformNames {
    Android, IOS
}