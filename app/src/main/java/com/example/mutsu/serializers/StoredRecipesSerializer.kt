package com.example.mutsu.serializers

import androidx.datastore.core.Serializer
import com.example.mutsu.StoredRecipes
import java.io.InputStream
import java.io.OutputStream

// Proto DataStore Serializer idea came from https://developer.android.com/codelabs/android-proto-datastore#5
class StoredRecipesSerializer : Serializer<StoredRecipes> {
    override val defaultValue: StoredRecipes = StoredRecipes.getDefaultInstance()

        override suspend fun readFrom(input: InputStream): StoredRecipes {
            return StoredRecipes.parseFrom(input)
        }

        override suspend fun writeTo(t: StoredRecipes, output: OutputStream) {
            t.writeTo(output)
        }
}