package com.gaoyun.roar.model.domain

enum class LanguageCode {
    English, Russian, German
}

fun String.toLanguageCode(): LanguageCode = when(this) {
    "ru" -> LanguageCode.Russian
    "de" -> LanguageCode.German
    else -> LanguageCode.English
}