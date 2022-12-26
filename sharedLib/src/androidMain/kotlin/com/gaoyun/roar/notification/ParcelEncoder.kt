package com.gaoyun.roar.notification

import android.os.Parcel
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.modules.EmptySerializersModule

@ExperimentalSerializationApi
internal class ParcelEncoder(private val parcel: Parcel) : AbstractEncoder() {

    override val serializersModule = EmptySerializersModule

    override fun beginCollection(descriptor: SerialDescriptor, collectionSize: Int): CompositeEncoder {
        return super.beginCollection(descriptor, collectionSize).also {
            parcel.writeInt(collectionSize)
        }
    }

    override fun endStructure(descriptor: SerialDescriptor) {
    }

    override fun encodeNull() = parcel.writeByte(0)
    override fun encodeNotNullMark() = parcel.writeByte(1)
    override fun encodeBoolean(value: Boolean) = parcel.writeByte(if (value) 1 else 0)
    override fun encodeByte(value: Byte) = parcel.writeByte(value)
    override fun encodeShort(value: Short) = parcel.writeInt(value.toInt())
    override fun encodeInt(value: Int) = parcel.writeInt(value)
    override fun encodeLong(value: Long) = parcel.writeLong(value)
    override fun encodeFloat(value: Float) = parcel.writeFloat(value)
    override fun encodeDouble(value: Double) = parcel.writeDouble(value)
    override fun encodeChar(value: Char) = parcel.writeInt(value.code)
    override fun encodeString(value: String) = parcel.writeString(value)
    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) = parcel.writeInt(index)
}

@OptIn(ExperimentalSerializationApi::class)
internal class ParcelDecoder(private val parcel: Parcel) : AbstractDecoder() {

    override val serializersModule = EmptySerializersModule

    override fun decodeSequentially(): Boolean = true
    override fun decodeElementIndex(descriptor: SerialDescriptor): Int = CompositeDecoder.DECODE_DONE
    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = parcel.readInt()
    override fun decodeNotNullMark(): Boolean = parcel.readByte() != 0.toByte()
    override fun decodeBoolean(): Boolean = parcel.readByte().toInt() != 0
    override fun decodeByte(): Byte = parcel.readByte()
    override fun decodeShort(): Short = parcel.readInt().toShort()
    override fun decodeInt(): Int = parcel.readInt()
    override fun decodeLong(): Long = parcel.readLong()
    override fun decodeFloat(): Float = parcel.readFloat()
    override fun decodeDouble(): Double = parcel.readDouble()
    override fun decodeChar(): Char = parcel.readInt().toChar()
    override fun decodeString(): String = parcel.readString()!!
    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int = parcel.readInt()
}
