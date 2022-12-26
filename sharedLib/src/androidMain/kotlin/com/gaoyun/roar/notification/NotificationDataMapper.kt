package com.gaoyun.roar.notification

import android.os.Parcel
import androidx.work.Data
import com.gaoyun.roar.model.domain.NotificationData
import com.gaoyun.roar.model.domain.NotificationItem
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy

private const val KEY = "notification"

fun NotificationItem.toInputData(scheduled: LocalDateTime): Data {
    val inputData = NotificationData(this, scheduled)

    return Data.Builder()
        .putByteArray(KEY, inputData.serialize(NotificationData.serializer()))
        .build()
}

fun Data.toNotificationData(): NotificationData {
    return getByteArray(KEY)!!.deSerialize(NotificationData.serializer())
}

@OptIn(ExperimentalSerializationApi::class)
fun <T> T.serialize(serializer: SerializationStrategy<T>): ByteArray {
    val parcel = Parcel.obtain()
    serializer.serialize(ParcelEncoder(parcel), this)
    return parcel.marshall()
}

fun <T> ByteArray.deSerialize(deserializer: DeserializationStrategy<T>): T {
    val parcel = Parcel.obtain()
    parcel.unmarshall(this, 0, size)
    parcel.setDataPosition(0)
    return deserializer.deserialize(ParcelDecoder(parcel))
}