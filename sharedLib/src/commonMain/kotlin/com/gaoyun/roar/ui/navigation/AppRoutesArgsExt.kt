package com.gaoyun.roar.ui.navigation

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

inline fun <reified T> serializableNavType(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
): NavType<T> = object : NavType<T>(isNullableAllowed) {
    private val innerSerializer: KSerializer<T> = serializer()

    override fun get(bundle: SavedState, key: String): T? {
        return bundle.read { this.getStringOrNull(key) }?.let { stringValue ->
            try {
                json.decodeFromString(innerSerializer, stringValue)
            } catch (e: SerializationException) {
                throw IllegalArgumentException("Failed to deserialize ${T::class.simpleName}", e)
            }
        }
    }

    override fun put(bundle: SavedState, key: String, value: T) {
        try {
            bundle.write { this.putString(key, json.encodeToString(innerSerializer, value)) }
        } catch (e: SerializationException) {
            throw IllegalArgumentException("Failed to serialize ${T::class.simpleName}", e)
        }
    }

    override fun parseValue(value: String): T {
        return try {
            json.decodeFromString(innerSerializer, value)
        } catch (e: SerializationException) {
            throw IllegalArgumentException("Failed to parse ${T::class.simpleName}", e)
        }
    }

    override fun serializeAsValue(value: T): String {
        return try {
            json.encodeToString(innerSerializer, value)
        } catch (e: SerializationException) {
            throw IllegalArgumentException("Failed to serialize ${T::class.simpleName}", e)
        }
    }
}