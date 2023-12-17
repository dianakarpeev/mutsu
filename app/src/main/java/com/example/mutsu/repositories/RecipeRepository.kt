package com.example.mutsu.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import com.example.mutsu.Measurements
import com.example.mutsu.Recipe
import com.example.mutsu.RecipeIngredient
import com.example.mutsu.StoredRecipe
import com.example.mutsu.StoredRecipes
import com.example.mutsu.TemporaryIngredient
import kotlinx.coroutines.flow.firstOrNull

class RecipeRepository(private val dataStore: DataStore<StoredRecipes>, context: Context) {

    val dataFlow = dataStore.data

    suspend fun doesRecipeExist(name: String) : Boolean {
        val recipe = getRecipeByName(name)
        return recipe != null
    }

    suspend fun getRecipeByName(name : String): StoredRecipe? {
        return dataFlow.firstOrNull()?.recipesList?.firstOrNull {recipe ->
            recipe.name == name
        }
    }

    suspend fun addRecipe(recipe: StoredRecipe, originalName: String = "") {
        dataStore.updateData { currentData ->
            val mutableList = currentData.recipesList.toMutableList()
            var index = -1;

            if (originalName != "") {
                index = mutableList.indexOfFirst { it.name == originalName }
            }

            if (index != -1) {
                mutableList[index] = recipe
            } else {
                mutableList.add(recipe)
            }

            currentData.toBuilder().clearRecipes().addAllRecipes(mutableList).build()
        }
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

    fun createStoredRecipe(
        recipe: Recipe,
        storedIngredients: List<RecipeIngredient>
    ): StoredRecipe {
        return StoredRecipe.newBuilder()
            .setName(recipe.name)
            .setPortionYield(recipe.portionYield)
            .setWebURL(recipe.webURL ?: "")
            .addAllIngredients(storedIngredients)
            .build()
    }

    suspend fun deleteAllRecipes() {
        dataStore.updateData {
            it.toBuilder().clear().build()
        }
    }

    suspend fun deleteRecipe(recipeName : String) {
        dataStore.updateData { currentData ->
            val mutableList = currentData.recipesList.toMutableList()
            val index = mutableList.indexOfFirst { it.name == recipeName }
            if (index != -1) {
                mutableList.removeAt(index)
            }
            currentData.toBuilder().clearRecipes().addAllRecipes(mutableList).build()
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