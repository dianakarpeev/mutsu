package com.example.emptyactivity

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emptyactivity.repositories.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

//ViewModel responsible for managing Recipe data and CRUD operations.
class RecipeViewModel(datastore : DataStore<StoredRecipe>, context: Context) : ViewModel() {
    private val storedRecipes = RecipeRepository(datastore, context)
    private val recipesFlow = storedRecipes.dataFlow
    private val _recipeList = MutableStateFlow<List<Recipe>>(emptyList())
    val recipeList: StateFlow<List<Recipe>> = _recipeList.asStateFlow()

    private var editableList = mutableListOf<Recipe>()


    //Initializes the ViewModel with sample recipe data.
    init {
            //_recipeList.value = instantiateRecipes()
            viewModelScope.launch {

                val hardlist = instantiateRecipes()

                storedRecipes.deleteAllRecipes()
                seedRecipes(hardlist)

                recipesFlow.collect() { storedRecipe ->
                    val recipe = storedRecipes.parseRecipeData(storedRecipe)
                    if (recipe.ingredients.isNotEmpty()) {
                        //editableList.add(recipe)
                        addRecipe(recipe)
                    }
                }

            }
    }

    //Creates and returns a list of sample recipes.
    private fun instantiateRecipes(): List<Recipe> {
        val recipeSeedData = mutableListOf<Recipe>()

        recipeSeedData.add(Recipe("Pasta Carbonara", getIngredientsForCarbonara(), 4, "https://www.bonappetit.com/recipe/simple-carbonara"))
        recipeSeedData.add(Recipe("Bread Pudding", getIngredientsForBreadPudding(), 8, "https://fantabulosity.com/last-minute-bread-pudding/"))
        recipeSeedData.add(Recipe("Apple Pie", getIngredientsForApplePie(), 8, ""))
        recipeSeedData.add(Recipe("Pancakes", getIngredientsForPancakes(), 4, ""))
        recipeSeedData.add(Recipe("Hot Dogs", getIngredientsForHotDogs(), 1, ""))

        return recipeSeedData
    }

    private fun seedRecipes(recipes : List<Recipe>) {
        viewModelScope.launch {
            recipes.forEach { recipe ->
                if(!isRecipeInStorage(recipe.name)) {
                    val ings = storedRecipes.createStoredIngredients(recipe)
                    val storedRecipe = storedRecipes.createStoredRecipe(recipe, ings)
                    storedRecipes.dataStore.updateData { storedRecipe }
                }
            }
        }
    }

    fun isRecipeInStorage(name: String) : Boolean {
        var exists = false
        viewModelScope.launch {
            try {
                val recipeData = recipesFlow.filter { storedRecipe -> storedRecipe.name == name }
                val recipe = recipeData.first()
                if (recipe.name == name) {
                    exists = true
                }
            } catch (e: Exception) {
                exists = false
            }
        }
        return exists
    }

    //region Recipe Data
    private fun getIngredientsForCarbonara() : MutableList<TemporaryIngredient>{
        val ingredientList = mutableListOf<TemporaryIngredient>()

        ingredientList.add(TemporaryIngredient(500.0, Measurements.GRAM, "Spaghetti"))
        ingredientList.add(TemporaryIngredient(200.0, Measurements.GRAM, "Bacon"))
        ingredientList.add(TemporaryIngredient(2.0, Measurements.NONE, "Egg"))
        ingredientList.add(TemporaryIngredient(0.5, Measurements.CUP, "Parmesan"))
        ingredientList.add(TemporaryIngredient(1.0, Measurements.TEASPOON, "Black pepper"))

        return ingredientList
    }

    private fun getIngredientsForBreadPudding() : MutableList<TemporaryIngredient>{
        val ingredientList = mutableListOf<TemporaryIngredient>()

        ingredientList.add(TemporaryIngredient(6.0, Measurements.NONE, "Slices of bread"))
        ingredientList.add(TemporaryIngredient(4.0, Measurements.NONE, "Egg"))
        ingredientList.add(TemporaryIngredient(3.0, Measurements.TABLESPOON, "Butter"))
        ingredientList.add(TemporaryIngredient(2.0, Measurements.CUP, "Milk"))
        ingredientList.add(TemporaryIngredient(0.5, Measurements.TEASPOON, "Cinnamon"))
        ingredientList.add(TemporaryIngredient(1.0, Measurements.TEASPOON, "Vanilla extract"))

        return ingredientList
    }

    private fun getIngredientsForApplePie() : MutableList<TemporaryIngredient> {
        val ingredientList = mutableListOf<TemporaryIngredient>()

        ingredientList.add(TemporaryIngredient(1.0, Measurements.NONE, "Pie crust"))
        ingredientList.add(TemporaryIngredient(6.0, Measurements.NONE, "Apples"))
        ingredientList.add(TemporaryIngredient(0.5, Measurements.CUP, "Sugar"))
        ingredientList.add(TemporaryIngredient(0.5, Measurements.CUP, "Brown sugar"))
        ingredientList.add(TemporaryIngredient(1.0, Measurements.TEASPOON, "Cinnamon"))
        ingredientList.add(TemporaryIngredient(0.5, Measurements.TEASPOON, "Nutmeg"))

        return ingredientList
    }

    private fun getIngredientsForPancakes() : MutableList<TemporaryIngredient> {
        val ingredientList = mutableListOf<TemporaryIngredient>()

        ingredientList.add(TemporaryIngredient(1.0, Measurements.NONE, "Egg"))
        ingredientList.add(TemporaryIngredient(1.0, Measurements.CUP, "Flour"))
        ingredientList.add(TemporaryIngredient(1.0, Measurements.CUP, "Milk"))
        ingredientList.add(TemporaryIngredient(1.0, Measurements.TEASPOON, "Baking powder"))
        ingredientList.add(TemporaryIngredient(1.0, Measurements.TEASPOON, "Vanilla extract"))

        return ingredientList
    }

    private fun getIngredientsForHotDogs() : MutableList<TemporaryIngredient> {
        val ingredientList = mutableListOf<TemporaryIngredient>()

        ingredientList.add(TemporaryIngredient(1.0, Measurements.NONE, "Hot dog buns"))
        ingredientList.add(TemporaryIngredient(1.0, Measurements.NONE, "Hot dogs"))
        ingredientList.add(TemporaryIngredient(1.0, Measurements.NONE, "Ketchup"))

        return ingredientList
    }
    //endregion

    /**
     * Adds a new recipe to the recipe list.
     *
     * @param recipe The recipe to be added.
     */
    fun addRecipe() {
        viewModelScope.launch {
            _recipeList.update { recipes -> emptyList() }
            _recipeList.update { recipes -> copyList() }
        }
    }

    fun addRecipe(recipe: Recipe) {
        viewModelScope.launch {

            val newList = _recipeList.value.toMutableList()
            newList.add(recipe)

            _recipeList.update { recipes -> emptyList() }
            _recipeList.update { recipes -> newList }
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

    private fun copyList() : List<Recipe> {
        var list = mutableListOf<Recipe>()

        for (i in editableList){
            list.add(Recipe(i.name, i.ingredients, i.portionYield, i.webURL))
        }

        return list

    }
}
