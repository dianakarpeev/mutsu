package com.example.emptyactivity.serializers

import androidx.datastore.core.Serializer
import com.example.emptyactivity.StoredRecipes
import java.io.InputStream
import java.io.OutputStream

class StoredRecipesSerializer : Serializer<StoredRecipes> {
    override val defaultValue: StoredRecipes = StoredRecipes.getDefaultInstance()

        override suspend fun readFrom(input: InputStream): StoredRecipes {
            return StoredRecipes.parseFrom(input)
        }

        override suspend fun writeTo(t: StoredRecipes, output: OutputStream) {
            t.writeTo(output)
        }
}