package com.example.mutsu.repositories

import androidx.datastore.core.DataStore
import com.example.mutsu.StoredMealPlan
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.first

class StoredMealPlanRepository(val dataStore: DataStore<StoredMealPlan>) {
        val dataFlow = dataStore.data

        suspend fun addMealPlan(mealPlan: Map<String, Int>) {
            val currentMap = getMealPlanAsMap().toMutableMap()

            mealPlan.forEach { (recipeName, count) ->
                currentMap[recipeName] = count
            }

            dataStore.updateData { currentData ->
                currentData.toBuilder().putAllRecipeNamesAndCount(currentMap).build()
            }

            val test = dataFlow.firstOrNull()?.recipeNamesAndCountMap ?: emptyMap()
            if (test.isNotEmpty()) {
                println(test)
                println("test")
            } else {
                println("test2")
            }
        }

        suspend fun getMealPlanAsMap() : Map<String, Int> {
            return dataFlow.firstOrNull()?.recipeNamesAndCountMap ?: emptyMap()
        }

        suspend fun deleteMealPlan() {
            dataStore.updateData { currentData ->
                currentData.toBuilder().clearRecipeNamesAndCount().build()
            }
        }
}