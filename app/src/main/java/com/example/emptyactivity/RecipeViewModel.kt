package com.example.emptyactivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {
    private val _recipeList = MutableStateFlow<List<Recipe>>(emptyList())
    val recipeList: StateFlow<List<Recipe>> = _recipeList.asStateFlow()

    init {
        _recipeList.value = instantiateRecipes()
    }

    private fun instantiateRecipes(): List<Recipe> {
        val recipeSeedData = mutableListOf<Recipe>()

        recipeSeedData.add(
            Recipe(
                "Pasta Carbonara",
                getIngredientsForCarbonara(),
                4,
                "https://www.bonappetit.com/recipe/simple-carbonara"
            )
        )
        recipeSeedData.add(
            Recipe(
                "Bread Pudding",
                getIngredientsForBreadPudding(),
                8,
                "https://fantabulosity.com/last-minute-bread-pudding/"
            )
        )

        return recipeSeedData
    }

    private fun getIngredientsForCarbonara() : MutableList<TemporaryIngredient>{
        val ingredientList = mutableListOf<TemporaryIngredient>()

        ingredientList.add(
            TemporaryIngredient(
                name = "Spaghetti",
                measurement = Measurements.GRAM,
                quantity = 500
            )
        )

        ingredientList.add(
            TemporaryIngredient(
                name = "Bacon",
                measurement = Measurements.GRAM,
                quantity = 200
            )
        )

        ingredientList.add(
            TemporaryIngredient(
                name = "Egg",
                measurement = Measurements.NONE,
                quantity = 2
            )
        )

        ingredientList.add(
            TemporaryIngredient(
                name = "Parmesan",
                measurement = Measurements.CUP,
                quantity = 1/2
            )
        )

        ingredientList.add(
            TemporaryIngredient(
                name = "Black pepper",
                measurement = Measurements.TEASPOON,
                quantity = 1
            )
        )

        return ingredientList
    }

    private fun getIngredientsForBreadPudding() : MutableList<TemporaryIngredient>{
        var ingredientList = mutableListOf<TemporaryIngredient>()

        ingredientList.add(
            TemporaryIngredient(
                name = "Slices of bread",
                measurement = Measurements.NONE,
                quantity = 6
            )
        )

        ingredientList.add(
            TemporaryIngredient(
                name = "Egg",
                measurement = Measurements.NONE,
                quantity = 4
            )
        )

        ingredientList.add(
            TemporaryIngredient(
                name = "Butter",
                measurement = Measurements.TABLESPOON,
                quantity = 3
            )
        )

        ingredientList.add(
            TemporaryIngredient(
                name = "Milk",
                measurement = Measurements.CUP,
                quantity = 2
            )
        )

        ingredientList.add(
            TemporaryIngredient(
                name = "Cinnamon",
                measurement = Measurements.TEASPOON,
                quantity = 1/2
            )
        )

        ingredientList.add(
            TemporaryIngredient(
                name = "Vanilla extract",
                measurement = Measurements.TEASPOON,
                quantity = 1
            )
        )

        return ingredientList
    }

    fun addRecipe(recipe: Recipe) {
        viewModelScope.launch {
            _recipeList.value = _recipeList.value + recipe
        }
    }

    fun removeRecipe(recipe: Recipe) {
        viewModelScope.launch {
            _recipeList.value = _recipeList.value - recipe
        }
    }

    fun getAllRecipes(): List<Recipe>{
        return recipeList.value;
    }

    fun getRecipeByName(name: String): Recipe? {
        return _recipeList.value.find { it.name == name }
    }

    fun editRecipe(recipeName: String, updatedRecipe: Recipe) {
        viewModelScope.launch {
            val newList = _recipeList.value.toMutableList()
            val index = newList.indexOfFirst { it.name == recipeName }
            if (index != -1) {
                newList[index] = updatedRecipe
                _recipeList.value = newList
            }
        }
    }
}
