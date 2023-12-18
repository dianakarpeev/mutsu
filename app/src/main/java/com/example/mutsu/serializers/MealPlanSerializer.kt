package com.example.mutsu.serializers

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.mutsu.StoredMealPlan
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

// Proto DataStore Serializer idea came from https://developer.android.com/codelabs/android-proto-datastore#5
class StoredMealPlanSerializer: Serializer<StoredMealPlan> {
override val defaultValue: StoredMealPlan = StoredMealPlan.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): StoredMealPlan {
        try {
            return StoredMealPlan.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: StoredMealPlan, output: OutputStream) {
        t.writeTo(output)
    }

}