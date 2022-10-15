package com.gaoyun.roar.util

object PreferencesKeys {
    const val AIRPORTS_LAST_FETCH_MILLIS = "AIRPORTS_LAST_FETCH_MILLIS"
    const val AIRPORTS_SAVED_ORIGINS = "AIRPORTS_SAVED_ORIGINS"
    const val AIRPORTS_SAVED_DESTINATIONS = "AIRPORTS_SAVED_DESTINATIONS"
    const val FLIGHT_MODE_SEARCH = "FLIGHT_MODE_SEARCH"
    const val ADULTS_NUMBER_SEARCH = "ADULTS_NUMBER_SEARCH"
    const val CHILDREN_NUMBER_SEARCH = "CHILDREN_NUMBER_SEARCH"
    const val DEPARTURE_DATE_FROM = "DEPARTURE_DATE_FROM"
    const val ARRIVE_DATE_FROM = "ARRIVE_DATE_FROM"
    const val DEPARTURE_DATE_TO = "DEPARTURE_DATE_TO"
    const val ARRIVE_DATE_TO = "ARRIVE_DATE_TO"
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
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