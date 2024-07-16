package com.gaoyun.roar.util

import android.content.Context
import android.content.SharedPreferences

actual class Preferences actual constructor(name: String?) {

    private val sharedPreferences: SharedPreferences = application.getSharedPreferences(name, Context.MODE_PRIVATE)

    actual fun setInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    actual fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    actual fun getInt(key: String): Int? {
        return if (hasKey(key)) {
            sharedPreferences.getInt(key, 0)
        } else {
            null
        }
    }

    actual fun setFloat(key: String, value: Float) {
        sharedPreferences.edit().putFloat(key, value).apply()
    }

    actual fun getFloat(key: String, defaultValue: Float): Float {
        return sharedPreferences.getFloat(key, defaultValue)
    }

    actual fun getFloat(key: String): Float? {
        return if (hasKey(key)) {
            sharedPreferences.getFloat(key, 0f)
        } else {
            null
        }
    }

    actual fun setLong(key: String, value: Long) {
        sharedPreferences.edit().putLong(key, value).apply()
    }

    actual fun getLong(key: String, defaultValue: Long): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    actual fun getLong(key: String): Long? {
        return if (hasKey(key)) {
            sharedPreferences.getLong(key, 0)
        } else {
            null
        }
    }

    actual fun setString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    actual fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    actual fun getString(key: String): String? {
        return if (hasKey(key)) {
            sharedPreferences.getString(key, "")
        } else {
            null
        }
    }

    actual fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    actual fun getBoolean(key: String): Boolean? {
        return if (hasKey(key)) {
            sharedPreferences.getBoolean(key, false)
        } else {
            null
        }
    }

    actual fun setBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    actual fun hasKey(key: String): Boolean = sharedPreferences.contains(key)

    actual fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    actual fun remove(key: String) {
        if (hasKey(key))
            sharedPreferences.edit().remove(key).apply()
    }
}