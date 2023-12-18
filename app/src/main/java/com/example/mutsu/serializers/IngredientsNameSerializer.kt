package com.example.mutsu.serializers

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.mutsu.IngredientsName
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

// Proto DataStore Serializer idea came from https://developer.android.com/codelabs/android-proto-datastore#5
class IngredientsNameSerializer : Serializer<IngredientsName> {
    override val defaultValue: IngredientsName = IngredientsName.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): IngredientsName {
        try {
            return IngredientsName.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: IngredientsName, output: OutputStream) {
        t.writeTo(output)
    }
}