package com.gaoyun.roar.util

object PreferencesKeys {
    const val CURRENT_USER_ID = "CURRENT_USER_ID"
    const val PET_BREEDS_LAST_UPDATE = "PET_BREEDS_LAST_UPDATE"
    const val PET_BREEDS_LOCALE = "PET_BREEDS_LOCALE"
    const val INTERACTION_TEMPLATES_LAST_UPDATE = "INTERACTION_TEMPLATES_LAST_UPDATE"
    const val ONBOARDING_COMPLETE = "ONBOARDING_COMPLETE"
    const val DYNAMIC_COLORS_ACTIVE = "DYNAMIC_COLORS_ACTIVE"
    const val COLOR_THEME = "COLOR_THEME"
    const val NUMBER_OF_REMINDERS_ON_MAIN_SCREEN = "NUMBER_OF_REMINDERS_ON_MAIN_SCREEN"
    const val LAST_SYNCHRONISED_TIMESTAMP = "LAST_SYNCHRONISED_TIMESTAMP"
    const val LAST_SYNCHRONISED_HASH = "LAST_SYNCHRONISED_HASH"
    const val HOME_SCREEN_MODE_FULL = "HOME_SCREEN_MODE_FULL"
    const val LAST_UPDATE_CHECK_DATETIME = "LAST_UPDATE_CHECK_DATETIME"
    const val SHOW_CUSTOMIZATION_PROMPT = "SHOW_CUSTOMIZATION_PROMPT"
}

expect class Preferences(name: String? = null) {

    fun setInt(key: String, value: Int)
    fun getInt(key: String, defaultValue: Int): Int
    fun getInt(key: String): Int?

    fun setFloat(key: String, value: Float)
    fun getFloat(key: String, defaultValue: Float): Float
    fun getFloat(key: String): Float?

    fun setLong(key: String, value: Long)
    fun getLong(key: String, defaultValue: Long): Long
    fun getLong(key: String): Long?

    fun setString(key: String, value: String)
    fun getString(key: String, defaultValue: String): String
    fun getString(key: String): String?

    fun setBoolean(key: String, value: Boolean)
    fun getBoolean(key: String, defaultValue: Boolean): Boolean
    fun getBoolean(key: String): Boolean?

    fun remove(key: String)
    fun clear()

    fun hasKey(key: String): Boolean
}