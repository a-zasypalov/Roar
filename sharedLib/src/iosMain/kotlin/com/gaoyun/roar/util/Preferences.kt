package com.gaoyun.roar.util

import platform.Foundation.NSUserDefaults

actual class Preferences actual constructor(name: String?) {

    private val userDefault: NSUserDefaults = NSUserDefaults(suiteName = name)

    actual fun setInt(key: String, value: Int) {
        return userDefault.setInteger(value.toLong(), key)
    }

    actual fun getInt(key: String, defaultValue: Int): Int {
        return if (hasKey(key))
            userDefault.integerForKey(key).toInt()
        else defaultValue
    }

    actual fun getInt(key: String): Int? {
        return if (hasKey(key))
            userDefault.integerForKey(key).toInt()
        else null
    }

    actual fun setFloat(key: String, value: Float) {
        return userDefault.setFloat(value, key)
    }

    actual fun getFloat(key: String, defaultValue: Float): Float {
        return if (hasKey(key))
            userDefault.floatForKey(key)
        else defaultValue
    }

    actual fun getFloat(key: String): Float? {
        return if (hasKey(key)) {
            userDefault.floatForKey(key)
        } else {
            null
        }
    }

    actual fun setLong(key: String, value: Long) {
        return userDefault.setInteger(value, key)
    }

    actual fun getLong(key: String, defaultValue: Long): Long {
        return if (hasKey(key))
            userDefault.integerForKey(key)
        else defaultValue
    }

    actual fun getLong(key: String): Long? {
        return if (hasKey(key)) {
            userDefault.integerForKey(key)
        } else {
            null
        }
    }

    actual fun setString(key: String, value: String) {
        return userDefault.setObject(value, key)
    }

    actual fun getString(key: String, defaultValue: String): String {
        return userDefault.stringForKey(key) ?: defaultValue
    }

    actual fun getString(key: String): String? {
        return if (hasKey(key)) {
            userDefault.stringForKey(key) ?: ""
        } else {
            null
        }
    }

    actual fun setBoolean(key: String, value: Boolean) {
        return userDefault.setBool(value, key)
    }

    actual fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return if (hasKey(key))
            userDefault.boolForKey(key)
        else defaultValue
    }

    actual fun getBoolean(key: String): Boolean? {
        return if (hasKey(key)) {
            userDefault.boolForKey(key)
        } else {
            null
        }
    }

    actual fun hasKey(key: String): Boolean = userDefault.objectForKey(key) != null

    actual fun clear() {
        for (key in userDefault.dictionaryRepresentation().keys) {
            remove(key as String)
        }
    }

    actual fun remove(key: String) {
        userDefault.removeObjectForKey(key)
    }

}