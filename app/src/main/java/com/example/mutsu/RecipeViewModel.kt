package com.example.mutsu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//ViewModel responsible for managing Recipe data and CRUD operations.
class RecipeViewModel : ViewModel() {
    private val _recipeList = MutableStateFlow<List<Recipe>>(emptyList())
    private val recipeList: StateFlow<List<Recipe>> = _recipeList.asStateFlow()

    //Initializes the ViewModel with sample recipe data.
    init {
        _recipeList.value = instantiateRecipes()
    }

    //Creates and returns a list of sample recipes.
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
                quantity = 500.0
            )
        )

        ingredientList.add(
            TemporaryIngredient(
                name = "Bacon",
                measurement = Measurements.GRAM,
                quantity = 200.0
            )
        )

        ingredientList.add(
            TemporaryIngredient(
                name = "Egg",
                measurement = Measurements.NONE,
                quantity = 2.0
            )
        )

        ingredientList.add(
            TemporaryIngredient(
                name = "Parmesan",
                measurement = Measurements.CUP,
                quantity = 0.5
            )
        )

        ingredientList.add(
            TemporaryIngredient(
                name = "Black pepper",
                measurement = Measurements.TEASPOON,
                quantity = 1.0
            )
        )

        return ingredientList
    }

    private fun getIngredientsForBreadPudding() : MutableList<TemporaryIngredient>{
        val ingredientList = mutableListOf<TemporaryIngredient>()

        ingredientList.add(
            TemporaryIngredient(
                name = "Slices of bread",
                measurement = Measurements.NONE,
                quantity = 6.0
            )
        )

        ingredientList.add(
            TemporaryIngredient(
                name = "Egg",
                measurement = Measurements.NONE,
                quantity = 4.0
            )
        )

        ingredientList.add(
            TemporaryIngredient(
                name = "Butter",
                measurement = Measurements.TABLESPOON,
                quantity = 3.0
            )
        )

        ingredientList.add(
            TemporaryIngredient(
                name = "Milk",
                measurement = Measurements.CUP,
                quantity = 2.0
            )
        )

        ingredientList.add(
            TemporaryIngredient(
                name = "Cinnamon",
                measurement = Measurements.TEASPOON,
                quantity = 0.5
            )
        )

        ingredientList.add(
            TemporaryIngredient(
                name = "Vanilla extract",
                measurement = Measurements.TEASPOON,
                quantity = 1.0
            )
        )

        return ingredientList
    }

    /**
     * Adds a new recipe to the recipe list.
     *
     * @param recipe The recipe to be added.
     */
    fun addRecipe(recipe: Recipe) {
        viewModelScope.launch {
            _recipeList.value = _recipeList.value + recipe
        }
    }

    /**
     * Removes a recipe from the recipe list.
     *
     * @param recipe The recipe to be removed.
     */
    fun removeRecipe(recipe: Recipe) {
        viewModelScope.launch {
            _recipeList.value = _recipeList.value - recipe
        }
    }

    /**
     * Retrieves all recipes from the recipe list.
     *
     * @return A list of all recipes.
     */
    fun getAllRecipes(): List<Recipe>{
        return recipeList.value
    }

    /**
     * Retrieves a recipe by its name.
     *
     * @param name The name of the recipe to retrieve.
     * @return The recipe with the specified name, or null if not found.
     */
    fun getRecipeByName(name: String): Recipe? {
        return _recipeList.value.find { it.name == name }
    }

    /**
     * Updates an existing recipe in the recipe list.
     *
     * @param recipeName The name of the recipe to be updated.
     * @param updatedRecipe The updated recipe information.
     */
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
