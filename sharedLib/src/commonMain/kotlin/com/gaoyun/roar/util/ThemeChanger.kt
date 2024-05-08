package com.gaoyun.roar.util

interface ThemeChanger {
    fun applyTheme()
    fun activateIcon(icon: AppIcon)
}

enum class AppIcon {
    Roar, Paw
}