package com.example.mutsu.repositories

import androidx.datastore.core.DataStore
import com.example.mutsu.StoredMealPlan
import kotlinx.coroutines.flow.firstOrNull

class StoredMealPlanRepository(val dataStore: DataStore<StoredMealPlan>) {
        val dataFlow = dataStore.data

         suspend fun addMealPlan(mealPlan: Map<String, Int>) {
            val currentMap = mutableMapOf<String, Int>()

            mealPlan.forEach { (recipeName, count) ->
                currentMap[recipeName] = count
            }

            dataStore.updateData { currentData ->
                currentData.toBuilder().putAllRecipeNamesAndCount(currentMap).build()
            }
        }

        suspend fun getMealPlanAsMap() : Map<String, Int> {

            val firstMap = dataFlow.firstOrNull()?.recipeNamesAndCountMap ?: emptyMap()

            return dataFlow.firstOrNull()?.recipeNamesAndCountMap ?: emptyMap()
        }

        suspend fun deleteMealPlan() {
            dataStore.updateData { currentData ->
                currentData.toBuilder().clearRecipeNamesAndCount().build()
            }
        }
}