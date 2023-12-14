package com.example.emptyactivity.serializers

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.emptyactivity.RecipeIngredient
import com.example.emptyactivity.StoredRecipe
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

class RecipeSerializer : Serializer<StoredRecipe> {
    override val defaultValue: StoredRecipe = StoredRecipe.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): StoredRecipe {
        try {
            return StoredRecipe.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: StoredRecipe, output: OutputStream) {
        t.writeTo(output)
    }
}

class RecipeIngredientSerializer : Serializer<RecipeIngredient> {
    override val defaultValue: RecipeIngredient = RecipeIngredient.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): RecipeIngredient {
        try {
            return RecipeIngredient.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: RecipeIngredient, output: OutputStream) {
        t.writeTo(output)
    }
}