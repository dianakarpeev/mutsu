package com.example.mutsu

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import com.example.mutsu.serializers.IngredientsNameSerializer
import com.example.mutsu.serializers.StoredMealPlanSerializer
import com.example.mutsu.serializers.StoredRecipesSerializer
import java.io.File

class dataStoreSingleton() {
    private var ingredientsNameStore: DataStore<IngredientsName>? = null
    private var recipesStore: DataStore<StoredRecipes>? = null
    private var mealPlanStore : DataStore<StoredMealPlan>? = null



    fun getIngredientsNameStore(context: Context): DataStore<IngredientsName> {
        if (ingredientsNameStore == null) {
            ingredientsNameStore = DataStoreFactory.create(
                serializer = IngredientsNameSerializer(),
                produceFile = { File(context.filesDir, "ingredients_name") }
            )
        }
        return ingredientsNameStore!!
    }

    fun getRecipesStore(context: Context): DataStore<StoredRecipes> {
        if (recipesStore == null) {
            recipesStore = DataStoreFactory.create(
                serializer = StoredRecipesSerializer(),
                produceFile = { File(context.filesDir, "recipes") }
            )
        }
        return recipesStore!!
    }

    fun getMealPlanStore(context: Context): DataStore<StoredMealPlan> {
        if (mealPlanStore == null) {
            mealPlanStore = DataStoreFactory.create(
                serializer = StoredMealPlanSerializer(),
                produceFile = { File(context.filesDir, "meal_plan") }
            )
        }
        return mealPlanStore!!
    }

}