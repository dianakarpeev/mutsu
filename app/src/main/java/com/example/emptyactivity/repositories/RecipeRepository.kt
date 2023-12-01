package com.example.emptyactivity.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import com.example.emptyactivity.Ingredient
import com.example.emptyactivity.Measurements
import com.example.emptyactivity.Recipe
import com.example.emptyactivity.RecipeIngredient
import com.example.emptyactivity.StoredRecipe
import com.example.emptyactivity.TemporaryIngredient
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

class RecipeRepository(private val dataStore: DataStore<StoredRecipe>, context: Context) {

    suspend fun getRecipeByName(name : String) : Recipe? {
        // Get data from dataStore
        val dataFlow = dataStore.data

        val recipeData = dataFlow
            .filter { storedRecipe -> storedRecipe.name == name }
            .map { storedRecipe -> parseRecipeData(storedRecipe) }
            .first()

        // Return Recipe
        return recipeData
    }

    suspend fun getRecipeNames() : List<String> {
        // Get data from dataStore
        val dataFlow = dataStore.data

        val recipeNames = dataFlow
            .map { storedRecipe -> storedRecipe.name }
            .toList()

        // Return Recipe
        return recipeNames
    }

    suspend fun getAllRecipes() : List<Recipe> {
        // Get data from dataStore
        val dataFlow = dataStore.data

        val recipes = dataFlow
            .map { storedRecipe -> parseRecipeData(storedRecipe) }
            .toList()

        return recipes.filterNotNull()
    }

    suspend fun putRecipe(recipe: Recipe) {
        // Get data from dataStore
        val dataFlow = dataStore.data

        // Convert Recipe to StoredRecipe
        val storedRecipe = StoredRecipe.newBuilder()
            .setName(recipe.name)
            .setPortionYield(recipe.portionYield)
            .setWebURL(recipe.webURL)
            .addAllIngredients( recipe.ingredients.map { ingredient ->
                RecipeIngredient.newBuilder()
                    .setName(ingredient.name)
                    .setMeasurement(ingredient.measurement.toString())
                    .setQuantity(ingredient.quantity)
                    .build()
            } )
            .build()
        // Put data into dataStore
        dataFlow.map { storedRecipe }
    }

    //Delete a recipe from the dataStore
    //Not sure if this works tbh -M
    suspend fun deleteRecipe(recipe: Recipe) {
        val dataFlow = dataStore.data.toList()

        val remainingRecipes = dataFlow.filter { storedRecipe -> storedRecipe.name != recipe.name }

        dataStore.data.map { remainingRecipes }
    }


    private fun parseRecipeData(data: StoredRecipe) : Recipe? {
        try {
            return Recipe(
                name = data.name,
                ingredients = parseIngredientData(data),
                portionYield = data.portionYield,
                webURL = data.webURL,
            )
        } catch (e: Exception) {
            return null
        }
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