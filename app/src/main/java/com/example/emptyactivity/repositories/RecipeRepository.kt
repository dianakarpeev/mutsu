package com.example.emptyactivity.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import com.example.emptyactivity.Measurements
import com.example.emptyactivity.Recipe
import com.example.emptyactivity.RecipeIngredient
import com.example.emptyactivity.StoredRecipe
import com.example.emptyactivity.TemporaryIngredient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

class RecipeRepository(val dataStore: DataStore<StoredRecipe>, context: Context) {

    val dataFlow = dataStore.data

    suspend fun doesRecipeExist(name: String) : Boolean {
        try {
            val recipeData = dataFlow.filter { storedRecipe -> storedRecipe.name == name }.first()
            return true
        } catch (e: Exception) {
            return false
        }
    }

    suspend fun getRecipeByName(name : String): StoredRecipe? {
        // Get data from dataStore

        val recipeData = dataFlow
            .filter { storedRecipe -> storedRecipe.name == name }

        val recipe = recipeData.firstOrNull()

        return recipe
    }

    suspend fun getRecipeNames() : List<String> {
        // Get data from dataStore

        val names = mutableListOf<String>()

        dataFlow.collect() { storedRecipe ->
                names.add(storedRecipe.name)
            }

        // Return Recipe
        return names
    }

    fun createStoredIngredients(recipe: Recipe) : List<RecipeIngredient> {
        val storedIngredients = mutableListOf<RecipeIngredient>()
        // Convert Ingredients to StoredIngredients
        recipe.ingredients.forEach { ingredient ->
            val storedIng = RecipeIngredient.newBuilder()
                .setName(ingredient.name)
                .setMeasurement(ingredient.measurement.toString())
                .setQuantity(ingredient.quantity)
                .build()
            storedIngredients.add(storedIng)
        }
        return storedIngredients
    }

    fun createStoredRecipe(recipe: Recipe, storedIngredients: List<RecipeIngredient>) : StoredRecipe {
        val storedRecipe = StoredRecipe.newBuilder()
            .setName(recipe.name)
            .setPortionYield(recipe.portionYield)
            .setWebURL(recipe.webURL)
            .addAllIngredients(storedIngredients)
            .build()
        return storedRecipe
    }

    //Delete a recipe from the dataStore
    //Not sure if this works tbh -M
    suspend fun deleteRecipe(recipe: Recipe) {
        val dataFlow = dataStore.data.toList()

        val remainingRecipes = dataFlow.filter { storedRecipe -> storedRecipe.name != recipe.name }

        dataStore.data.map { remainingRecipes }
    }

    suspend fun deleteAllRecipes() {
        dataStore.updateData {
            it.toBuilder().clear().build()
        }
    }

    fun parseRecipeData(data: StoredRecipe) : Recipe {
            return Recipe(
                name = data.name,
                ingredients = parseIngredientData(data),
                portionYield = data.portionYield,
                webURL = data.webURL,
            )
    }

    /*
        Function to take the data from the dataStore and convert it into a list of
        TemporaryIngredient objects to be used in the Recipe object
        Dangerous: May throw an exception if the data is not formatted correctly
    */
    private fun parseIngredientData (data: StoredRecipe) : MutableList<TemporaryIngredient> {
        val ingredients = data.ingredientsList
        val tempIngredients : MutableList<TemporaryIngredient> = mutableListOf()

        for (i in ingredients) {
            tempIngredients.add(
                TemporaryIngredient(
                    name = i.name,
                    measurement = Measurements.valueOf(i.measurement),
                    quantity = i.quantity
                )
            )
        }

        return tempIngredients
    }
}