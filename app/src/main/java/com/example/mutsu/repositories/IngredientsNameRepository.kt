package com.example.mutsu.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import com.example.mutsu.IngredientsName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException

//Idea for repositories came from: https://www.oneclickitsolution.com/blog/implementation-proto-datastore-in-android/
class IngredientsNameRepository(private val dataStore: DataStore<IngredientsName>, context: Context) {

    val ingredientsNameFlow : Flow<MutableMap<String, String>> = dataStore.data
        .catch { exception ->
            if (exception is IOException){
                emit(IngredientsName.getDefaultInstance())
            } else {
                throw exception
            }
        }
        .map { ingredientsName ->
            ingredientsName.namesMap
        }


    suspend fun parseIngredients(ingredients : List<String>) {
        val map = dataStore.data.firstOrNull()?.namesMap ?: emptyMap()
        val newMap = mutableMapOf<String, String>()

        ingredients.forEach { ingredient ->
            if (!map.containsKey(ingredient)){
                //Replace spaces with underscores and make all letters uppercase
                val key = ingredient.replace(" ", "_").uppercase()
                newMap[key] = ingredient
            }
        }
        putIngredientMap(newMap)
    }

    suspend fun seedMap(ingredients : Map<String, String>) {

        if (ingredients.isEmpty()) {
            val seedsMap = mutableMapOf<String, String>()
            seedsMap["BREAD"] = "Bread"
            seedsMap["BEEF_PATTY"] = "Beef Patty"
            seedsMap["TOMATO"] = "Tomato"
            seedsMap["ONION"] = "Onion"
            seedsMap["CABBAGE"] = "Cabbage"
            seedsMap["BEEF"] = "Beef"
            seedsMap["CARROT"] = "Carrot"
            seedsMap["POTATO"] = "Potato"
            seedsMap["BACON"] = "Bacon"
            seedsMap["EGG"] = "Egg"
            //seedsMap["FLOUR"] = "Flour"

            putIngredientMap(seedsMap)
        }
    }

    suspend fun putIngredientMap(map : Map<String, String>){
        dataStore.updateData { ingredientsName ->
            ingredientsName.toBuilder()
                .putAllNames(map)
                .build()
        }
    }

}